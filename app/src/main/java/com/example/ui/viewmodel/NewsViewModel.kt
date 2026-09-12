package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.LanguageTranslationService
import com.example.data.engine.TtsManager
import com.example.data.location.LocationResult
import com.example.data.location.NovyraLocationManager
import com.example.data.model.AdConfig
import com.example.data.model.Article
import com.example.data.model.LocationTier
import com.example.data.model.NewsSource
import com.example.data.model.SystemLog
import com.example.data.model.UserLocation
import com.example.data.repository.NewsRepository
import com.example.data.repository.SyncStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsRepository.getInstance(application)
    private val ttsManager = TtsManager(application)
    private val locationManager = NovyraLocationManager(application)

    val isPlayingAudio = ttsManager.isPlaying
    val currentAudioArticleId = ttsManager.currentArticleId

    private val _locationLoading = MutableStateFlow(false)
    val locationLoading: StateFlow<Boolean> = _locationLoading.asStateFlow()

    private val _locationMessage = MutableStateFlow<String?>(null)
    val locationMessage: StateFlow<String?> = _locationMessage.asStateFlow()

    private val _selectedTab = MutableStateFlow(LocationTab.ALL)
    val selectedTab: StateFlow<LocationTab> = _selectedTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    private val _isIngestionRunning = MutableStateFlow(false)
    val isIngestionRunning: StateFlow<Boolean> = _isIngestionRunning.asStateFlow()

    private val _ingestionBannerMsg = MutableStateFlow<String?>(null)
    val ingestionBannerMsg: StateFlow<String?> = _ingestionBannerMsg.asStateFlow()

    // Dialogs
    private val _showLocationDialog = MutableStateFlow(false)
    val showLocationDialog: StateFlow<Boolean> = _showLocationDialog.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _showAdminPanel = MutableStateFlow(false)
    val showAdminPanel: StateFlow<Boolean> = _showAdminPanel.asStateFlow()

    private val _showPrivacyDialog = MutableStateFlow(false)
    val showPrivacyDialog: StateFlow<Boolean> = _showPrivacyDialog.asStateFlow()

    // Preferences & Location
    val userPreferences = repository.userPreferences.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.example.data.local.UserPreferencesEntity()
    )

    val breakingNews: StateFlow<List<Article>> = repository.breakingNews.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val savedArticles: StateFlow<List<Article>> = repository.savedArticles.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allSources: StateFlow<List<NewsSource>> = repository.allSources.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val systemLogs: StateFlow<List<SystemLog>> = repository.systemLogs.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val adConfig: StateFlow<AdConfig> = repository.adConfig.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AdConfig()
    )

    val syncStatus: StateFlow<SyncStatus> = repository.syncStatus.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SyncStatus.Idle
    )

    val lastSyncTimestamp: StateFlow<Long> = repository.lastSyncTimestamp.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0L
    )

    // Filtered Feed combining articles, selectedTab, selectedCategory, and userLocation
    val feedArticles: StateFlow<List<Article>> = combine(
        repository.allArticles,
        _selectedTab,
        _selectedCategory,
        repository.userPreferences
    ) { articles, tab, category, prefs ->
        var list = articles

        // 1. Filter by location tier / tab
        if (prefs.isPersonalizationEnabled) {
            list = when (tab) {
                LocationTab.ALL -> list
                LocationTab.LOCAL -> list.filter {
                    it.locationTier == LocationTier.LOCAL ||
                    it.city.contains(prefs.city, ignoreCase = true) ||
                    it.district.contains(prefs.district, ignoreCase = true)
                }
                LocationTab.STATE -> list.filter {
                    it.locationTier == LocationTier.STATE ||
                    it.state.contains(prefs.state, ignoreCase = true)
                }
                LocationTab.NATIONAL -> list.filter {
                    it.locationTier == LocationTier.NATIONAL ||
                    it.country.contains(prefs.country, ignoreCase = true)
                }
                LocationTab.WORLD -> list.filter {
                    it.locationTier == LocationTier.WORLD
                }
                LocationTab.TRENDING -> list.filter { it.importanceScore >= 7 || it.viewsCount > 2000 }
            }
        } else {
            // When personalization disabled, generic tier mapping
            list = when (tab) {
                LocationTab.ALL -> list
                LocationTab.LOCAL -> list.filter { it.locationTier == LocationTier.LOCAL }
                LocationTab.STATE -> list.filter { it.locationTier == LocationTier.STATE }
                LocationTab.NATIONAL -> list.filter { it.locationTier == LocationTier.NATIONAL }
                LocationTab.WORLD -> list.filter { it.locationTier == LocationTier.WORLD }
                LocationTab.TRENDING -> list.filter { it.importanceScore >= 7 }
            }
        }

        // 2. Filter by category
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search results
    val searchResults: StateFlow<List<Article>> = combine(
        repository.allArticles,
        _searchQuery
    ) { articles, query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            val q = query.trim().lowercase()
            articles.filter {
                it.title.lowercase().contains(q) ||
                it.summary.lowercase().contains(q) ||
                it.city.lowercase().contains(q) ||
                it.state.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.tags.any { tag -> tag.lowercase().contains(q) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            // Fast first load: ensure Room cache is warm
            repository.seedInitialDataIfNeeded()
            // Background sync from remote News API if cache has expired
            if (repository.isCacheStale()) {
                repository.refreshNews(forceRefresh = false)
            }
        }
    }

    fun refreshNews(force: Boolean = true) {
        viewModelScope.launch {
            repository.refreshNews(forceRefresh = force)
        }
    }

    fun selectTab(tab: LocationTab) {
        _selectedTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun closeArticle() {
        _selectedArticle.value = null
        ttsManager.stop()
    }

    fun toggleSaveArticle(articleId: String, isCurrentlySaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSaveArticle(articleId, !isCurrentlySaved)
        }
    }

    fun toggleAudio(article: Article, langCode: String = "en") {
        if (isPlayingAudio.value && currentAudioArticleId.value == article.id) {
            ttsManager.stop()
        } else {
            val textToRead = "${article.title}. ${article.summary}. ${article.fullContent.take(500)}"
            ttsManager.speak(article.id, textToRead, langCode)
        }
    }

    fun stopAudio() {
        ttsManager.stop()
    }

    fun setLocation(country: String, state: String, city: String, enabled: Boolean = true) {
        viewModelScope.launch {
            repository.updateLocation(country, state, city, enabled)
            _showLocationDialog.value = false
        }
    }

    fun hasLocationPermission(): Boolean = locationManager.hasLocationPermission()

    fun detectCurrentLocation() {
        viewModelScope.launch {
            _locationLoading.value = true
            _locationMessage.value = "Acquiring GPS / network fix..."
            when (val result = locationManager.getCurrentLocation()) {
                is LocationResult.Success -> {
                    val loc = result.userLocation
                    repository.updateLocation(
                        country = loc.country,
                        state = loc.state,
                        city = loc.city,
                        enabled = true
                    )
                    _locationMessage.value = "Location updated: ${loc.city}, ${loc.state}"
                }
                is LocationResult.PermissionDenied -> {
                    _locationMessage.value = "Location permission denied. Please enable in device settings."
                }
                is LocationResult.Error -> {
                    _locationMessage.value = result.message
                }
            }
            _locationLoading.value = false
        }
    }

    fun clearLocationMessage() {
        _locationMessage.value = null
    }

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            repository.updateLanguage(langCode)
            _showLanguageDialog.value = false
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            repository.toggleDarkMode(isDark)
        }
    }

    fun setShowLocationDialog(show: Boolean) {
        _showLocationDialog.value = show
    }

    fun setShowLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun setShowAdminPanel(show: Boolean) {
        _showAdminPanel.value = show
    }

    fun setShowPrivacyDialog(show: Boolean) {
        _showPrivacyDialog.value = show
    }

    fun dismissIngestionBanner() {
        _ingestionBannerMsg.value = null
    }

    // Admin Actions
    fun triggerIngestion() {
        viewModelScope.launch {
            _isIngestionRunning.value = true
            try {
                val report = repository.triggerIngestionPipeline()
                _ingestionBannerMsg.value = "Pipeline complete: Added ${report.newArticlesCount} new stories, ${report.clusteredCount} clustered in ${report.timeElapsedMs}ms."
            } catch (e: Exception) {
                _ingestionBannerMsg.value = "Ingestion warning: ${e.message}"
            } finally {
                _isIngestionRunning.value = false
            }
        }
    }

    fun toggleSourceActive(sourceId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleSource(sourceId, isActive)
        }
    }

    fun toggleBreakingStatus(articleId: String, isBreaking: Boolean) {
        viewModelScope.launch {
            repository.toggleBreakingStatus(articleId, isBreaking)
        }
    }

    fun deleteArticle(articleId: String) {
        viewModelScope.launch {
            repository.deleteArticle(articleId)
            if (_selectedArticle.value?.id == articleId) {
                _selectedArticle.value = null
            }
        }
    }

    fun addCustomSource(name: String, feedUrl: String, category: String) {
        viewModelScope.launch {
            repository.addSource(
                NewsSource(
                    id = "src_custom_${System.currentTimeMillis()}",
                    name = name,
                    websiteUrl = feedUrl,
                    feedUrl = feedUrl,
                    licenseStatus = "Authorized User Added Feed",
                    category = category,
                    country = "Global"
                )
            )
        }
    }

    fun saveAdConfig(config: AdConfig) {
        viewModelScope.launch {
            repository.saveAdConfig(config)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}

enum class LocationTab(val label: String, val icon: String) {
    ALL("Top Stories", "📰"),
    LOCAL("Local", "📍"),
    STATE("State", "🏛️"),
    NATIONAL("National", "🇮🇳"),
    WORLD("World", "🌍"),
    TRENDING("Trending", "🔥")
}
