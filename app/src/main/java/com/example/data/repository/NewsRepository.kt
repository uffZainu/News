package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.engine.IngestionReport
import com.example.data.engine.NewsIngestionEngine
import com.example.data.local.AdConfigEntity
import com.example.data.local.AppDatabase
import com.example.data.local.ArticleEntity
import com.example.data.local.SavedArticleEntity
import com.example.data.local.SourceEntity
import com.example.data.local.SystemLogEntity
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.AdConfig
import com.example.data.model.Article
import com.example.data.model.NewsSource
import com.example.data.model.SystemLog
import com.example.data.remote.NewsRemoteDataSource
import com.example.data.seed.InitialDataSeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * State representing synchronization status between the remote News API and Room local cache.
 */
sealed class SyncStatus {
    object Idle : SyncStatus()
    object Syncing : SyncStatus()
    data class Success(val newArticlesCount: Int, val timestamp: Long) : SyncStatus()
    data class Offline(val message: String, val cachedCount: Int) : SyncStatus()
    data class Error(val message: String) : SyncStatus()
}

/**
 * Summary result returned when refreshing news from the network API into Room.
 */
sealed class SyncResult {
    data class Success(val count: Int, val isFromCacheOnly: Boolean = false) : SyncResult()
    data class Offline(val cachedCount: Int, val message: String) : SyncResult()
    data class Error(val message: String) : SyncResult()
}

/**
 * NewsRepository orchestrates data operations between remote News APIs and local Room database.
 *
 * Implements an Offline-First, "Fast First Load" caching strategy:
 * 1. Room is the Single Source of Truth for all UI readers. Flow emits cached data instantly.
 * 2. Background sync fetches fresh news from remote news API without blocking the initial UI rendering.
 * 3. Freshly fetched articles are upserted into Room, automatically triggering reactive UI updates.
 * 4. In offline conditions, the app continues serving cached articles seamlessly with zero crashes.
 */
class NewsRepository(
    private val database: AppDatabase,
    private val ingestionEngine: NewsIngestionEngine,
    private val remoteDataSource: NewsRemoteDataSource = NewsRemoteDataSource()
) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    // Cache Configuration
    private val CACHE_EXPIRY_MS = 15 * 60 * 1000L // 15-minute Time-To-Live (TTL)

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _lastSyncTimestamp = MutableStateFlow(0L)
    val lastSyncTimestamp: StateFlow<Long> = _lastSyncTimestamp.asStateFlow()

    // =========================================================================
    // Reactive Room Streams (Fast First Load: Emits cached data immediately)
    // =========================================================================

    val allArticles: Flow<List<Article>> = database.articleDao().getAllArticles().map { list ->
        list.map { it.toDomain() }
    }

    val breakingNews: Flow<List<Article>> = database.articleDao().getBreakingArticles().map { list ->
        list.map { it.toDomain() }
    }

    val savedArticles: Flow<List<Article>> = database.articleDao().getSavedArticles().map { list ->
        list.map { it.toDomain() }
    }

    val allSources: Flow<List<NewsSource>> = database.sourceDao().getAllSources().map { list ->
        list.map { it.toDomain() }
    }

    val systemLogs: Flow<List<SystemLog>> = database.systemLogDao().getRecentLogs().map { list ->
        list.map { SystemLog(it.id, it.timestamp, it.level, it.tag, it.message) }
    }

    val adConfig: Flow<AdConfig> = database.adConfigDao().getAdConfig().map { entity ->
        if (entity != null) {
            AdConfig(
                isHeaderAdEnabled = entity.isHeaderAdEnabled,
                isInFeedAdEnabled = entity.isInFeedAdEnabled,
                isArticleAdEnabled = entity.isArticleAdEnabled,
                sponsorName = entity.sponsorName,
                customNotice = entity.customNotice,
                adMobAppId = entity.adMobAppId,
                adMobBannerUnitId = entity.adMobBannerUnitId
            )
        } else {
            AdConfig()
        }
    }

    val userPreferences: Flow<UserPreferencesEntity> = database.userPrefDao().getUserPreferences().map {
        it ?: UserPreferencesEntity()
    }

    // =========================================================================
    // Caching Strategy & Network API Ingestion
    // =========================================================================

    /**
     * Checks if the local cache is stale or empty based on TTL.
     */
    suspend fun isCacheStale(): Boolean = withContext(Dispatchers.IO) {
        val count = database.articleDao().getArticlesCount()
        if (count == 0) return@withContext true

        val lastSync = _lastSyncTimestamp.value
        val now = System.currentTimeMillis()
        if (lastSync == 0L) {
            val latestArticleTime = database.articleDao().getLatestArticleTimestamp() ?: 0L
            return@withContext (now - latestArticleTime) > CACHE_EXPIRY_MS
        }
        return@withContext (now - lastSync) > CACHE_EXPIRY_MS
    }

    /**
     * Refreshes news from remote News API / feeds and persists into Room.
     *
     * Caching logic:
     * - If cache is valid and [forceRefresh] is false, serves from Room cache immediately.
     * - If network is unavailable or fails, gracefully falls back to Room cache without crashing.
     */
    suspend fun refreshNews(forceRefresh: Boolean = false): SyncResult = withContext(Dispatchers.IO) {
        val cachedCount = database.articleDao().getArticlesCount()

        // 1. Check if cache is still fresh and refresh not forced
        if (!forceRefresh && !isCacheStale()) {
            _syncStatus.value = SyncStatus.Success(0, _lastSyncTimestamp.value)
            return@withContext SyncResult.Success(cachedCount, isFromCacheOnly = true)
        }

        _syncStatus.value = SyncStatus.Syncing

        try {
            // 2. Fetch fresh news from active sources via Remote News API
            val activeSources = database.sourceDao().getActiveSources().map { it.toDomain() }
            val fetchedArticles = mutableListOf<Article>()

            if (activeSources.isNotEmpty()) {
                for (source in activeSources) {
                    val result = remoteDataSource.fetchArticlesFromSource(source)
                    result.onSuccess { articles ->
                        fetchedArticles.addAll(articles)
                        database.sourceDao().updateFetchStatus(
                            id = source.id,
                            timestamp = System.currentTimeMillis(),
                            status = "OK (${articles.size} cached)"
                        )
                    }.onFailure { error ->
                        Log.w("NewsRepository", "Remote fetch failed for ${source.name}: ${error.message}")
                    }
                }
            }

            // Also execute ingestion pipeline for structured multi-source clustering
            val report = ingestionEngine.runIngestionPipeline()
            val totalNew = fetchedArticles.size + report.newArticlesCount

            // 3. Persist fetched articles to Room database
            if (fetchedArticles.isNotEmpty()) {
                val entities = fetchedArticles.map { ArticleEntity.fromDomain(it) }
                database.articleDao().insertArticles(entities)
            }

            val updatedTime = System.currentTimeMillis()
            _lastSyncTimestamp.value = updatedTime
            _syncStatus.value = SyncStatus.Success(totalNew, updatedTime)

            database.systemLogDao().insertLog(
                SystemLogEntity(
                    level = "INFO",
                    tag = "NewsRepository",
                    message = "Cache updated from remote news API. Ingested $totalNew articles into Room."
                )
            )

            SyncResult.Success(totalNew, isFromCacheOnly = false)
        } catch (e: IOException) {
            // Offline scenario: Network unavailable
            Log.w("NewsRepository", "Network offline during refresh: ${e.message}")
            _syncStatus.value = SyncStatus.Offline("Offline: Showing cached news", cachedCount)

            database.systemLogDao().insertLog(
                SystemLogEntity(
                    level = "WARN",
                    tag = "NewsRepository",
                    message = "Device offline. Serving $cachedCount articles directly from Room local cache."
                )
            )

            SyncResult.Offline(cachedCount, "Operating in offline mode. Loaded $cachedCount cached articles.")
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error refreshing news: ${e.message}", e)
            _syncStatus.value = SyncStatus.Error(e.message ?: "Unknown error")
            SyncResult.Error(e.message ?: "Failed to refresh news")
        }
    }

    /**
     * Fast-first load pattern: Returns Room Flow immediately, triggering background refresh if stale.
     */
    fun getArticlesFastFirst(forceRefresh: Boolean = false): Flow<List<Article>> {
        repositoryScope.launch {
            if (forceRefresh || isCacheStale()) {
                refreshNews(forceRefresh)
            }
        }
        return allArticles
    }

    // =========================================================================
    // Query & Search Methods
    // =========================================================================

    fun getArticleById(id: String): Flow<Article?> {
        return database.articleDao().getArticleById(id).map { it?.toDomain() }
    }

    fun isArticleSaved(id: String): Flow<Boolean> {
        return database.savedArticleDao().isArticleSaved(id)
    }

    fun searchArticles(query: String): Flow<List<Article>> {
        return database.articleDao().searchArticles(query).map { list -> list.map { it.toDomain() } }
    }

    // =========================================================================
    // Mutations (Room Persistence)
    // =========================================================================

    suspend fun toggleSaveArticle(articleId: String, save: Boolean) = withContext(Dispatchers.IO) {
        if (save) {
            database.savedArticleDao().saveArticle(SavedArticleEntity(articleId))
        } else {
            database.savedArticleDao().removeSavedArticle(articleId)
        }
    }

    suspend fun updateLocation(country: String, state: String, city: String, enabled: Boolean) = withContext(Dispatchers.IO) {
        val current = database.userPrefDao().getUserPreferencesDirect() ?: UserPreferencesEntity()
        database.userPrefDao().saveUserPreferences(
            current.copy(
                country = country,
                state = state,
                city = city,
                district = city,
                isPersonalizationEnabled = enabled
            )
        )
    }

    suspend fun updateLanguage(langCode: String) = withContext(Dispatchers.IO) {
        val current = database.userPrefDao().getUserPreferencesDirect() ?: UserPreferencesEntity()
        database.userPrefDao().saveUserPreferences(current.copy(preferredLanguage = langCode))
    }

    suspend fun toggleDarkMode(isDark: Boolean) = withContext(Dispatchers.IO) {
        val current = database.userPrefDao().getUserPreferencesDirect() ?: UserPreferencesEntity()
        database.userPrefDao().saveUserPreferences(current.copy(isDarkMode = isDark))
    }

    suspend fun triggerIngestionPipeline(): IngestionReport {
        return ingestionEngine.runIngestionPipeline()
    }

    suspend fun toggleSource(id: String, isActive: Boolean) = withContext(Dispatchers.IO) {
        database.sourceDao().toggleSourceActive(id, isActive)
    }

    suspend fun addSource(source: NewsSource) = withContext(Dispatchers.IO) {
        database.sourceDao().insertSource(SourceEntity.fromDomain(source))
    }

    suspend fun deleteSource(id: String) = withContext(Dispatchers.IO) {
        database.sourceDao().deleteSource(id)
    }

    suspend fun toggleBreakingStatus(id: String, isBreaking: Boolean) = withContext(Dispatchers.IO) {
        database.articleDao().setBreakingStatus(id, isBreaking)
    }

    suspend fun deleteArticle(id: String) = withContext(Dispatchers.IO) {
        database.articleDao().deleteArticleById(id)
    }

    suspend fun saveAdConfig(adConfig: AdConfig) = withContext(Dispatchers.IO) {
        database.adConfigDao().saveAdConfig(
            AdConfigEntity(
                id = 1,
                isHeaderAdEnabled = adConfig.isHeaderAdEnabled,
                isInFeedAdEnabled = adConfig.isInFeedAdEnabled,
                isArticleAdEnabled = adConfig.isArticleAdEnabled,
                sponsorName = adConfig.sponsorName,
                customNotice = adConfig.customNotice,
                adMobAppId = adConfig.adMobAppId,
                adMobBannerUnitId = adConfig.adMobBannerUnitId
            )
        )
    }

    /**
     * Seeds initial data into Room on first application launch to ensure
     * that the 'fast first load' performance goal is met immediately even offline.
     */
    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val count = database.articleDao().getArticlesCount()
        if (count == 0) {
            database.sourceDao().insertSources(InitialDataSeed.sources.map { SourceEntity.fromDomain(it) })
            database.articleDao().insertArticles(InitialDataSeed.articles.map { ArticleEntity.fromDomain(it) })
            database.adConfigDao().saveAdConfig(AdConfigEntity())
            database.userPrefDao().saveUserPreferences(UserPreferencesEntity())
            _lastSyncTimestamp.value = System.currentTimeMillis()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getInstance(context: Context): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val engine = NewsIngestionEngine(db)
                val remote = NewsRemoteDataSource()
                val repo = NewsRepository(db, engine, remote)
                INSTANCE = repo
                repo
            }
        }
    }
}
