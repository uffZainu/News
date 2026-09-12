package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isBreaking = 1 ORDER BY publishedAt DESC")
    fun getBreakingArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE locationTier = :tier ORDER BY publishedAt DESC")
    fun getArticlesByLocationTier(tier: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: String): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getArticleByIdDirect(id: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE id IN (SELECT articleId FROM saved_articles) ORDER BY publishedAt DESC")
    fun getSavedArticles(): Flow<List<ArticleEntity>>

    @Query("""
        SELECT * FROM articles 
        WHERE title LIKE '%' || :query || '%' 
           OR summary LIKE '%' || :query || '%' 
           OR city LIKE '%' || :query || '%'
           OR state LIKE '%' || :query || '%'
           OR country LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR tags LIKE '%' || :query || '%'
        ORDER BY publishedAt DESC
    """)
    fun searchArticles(query: String): Flow<List<ArticleEntity>>

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticlesCount(): Int

    @Query("SELECT MAX(publishedAt) FROM articles")
    suspend fun getLatestArticleTimestamp(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Update
    suspend fun updateArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun deleteArticleById(id: String)

    @Query("UPDATE articles SET isBreaking = :isBreaking WHERE id = :id")
    suspend fun setBreakingStatus(id: String, isBreaking: Boolean)

    @Query("UPDATE articles SET importanceScore = :score WHERE id = :id")
    suspend fun updateImportanceScore(id: String, score: Int)
}

@Dao
interface SourceDao {
    @Query("SELECT * FROM news_sources ORDER BY name ASC")
    fun getAllSources(): Flow<List<SourceEntity>>

    @Query("SELECT * FROM news_sources WHERE isActive = 1")
    suspend fun getActiveSources(): List<SourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<SourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSource(source: SourceEntity)

    @Update
    suspend fun updateSource(source: SourceEntity)

    @Query("UPDATE news_sources SET isActive = :isActive WHERE id = :id")
    suspend fun toggleSourceActive(id: String, isActive: Boolean)

    @Query("UPDATE news_sources SET lastFetchedAt = :timestamp, fetchStatus = :status WHERE id = :id")
    suspend fun updateFetchStatus(id: String, timestamp: Long, status: String)

    @Query("DELETE FROM news_sources WHERE id = :id")
    suspend fun deleteSource(id: String)
}

@Dao
interface SavedArticleDao {
    @Query("SELECT * FROM saved_articles")
    fun getAllSavedRefs(): Flow<List<SavedArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE articleId = :articleId)")
    fun isArticleSaved(articleId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(savedArticle: SavedArticleEntity)

    @Query("DELETE FROM saved_articles WHERE articleId = :articleId")
    suspend fun removeSavedArticle(articleId: String)
}

@Dao
interface SystemLogDao {
    @Query("SELECT * FROM system_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<SystemLogEntity>>

    @Insert
    suspend fun insertLog(log: SystemLogEntity)

    @Query("DELETE FROM system_logs")
    suspend fun clearLogs()
}

@Dao
interface AdConfigDao {
    @Query("SELECT * FROM ad_configs WHERE id = 1")
    fun getAdConfig(): Flow<AdConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAdConfig(config: AdConfigEntity)
}

@Dao
interface UserPrefDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getUserPreferencesDirect(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(prefs: UserPreferencesEntity)
}
