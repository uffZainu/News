package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BreakingNewsBar
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.LocationSelectorDialog
import com.example.ui.components.TopHeader
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.screens.SavedArticlesScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.NovyraNewsTheme
import com.example.ui.viewmodel.NewsViewModel

enum class NavDestination(val label: String, val tag: String) {
    HOME("Home", "nav_home"),
    SEARCH("Search", "nav_search"),
    SAVED("Saved", "nav_saved"),
    ADMIN("Admin", "nav_admin")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.ui.components.NovyraAdMobManager.initialize(this)
        setContent {
            val viewModel: NewsViewModel = viewModel()
            val userPrefs by viewModel.userPreferences.collectAsState()

            NovyraNewsTheme(darkTheme = userPrefs.isDarkMode) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: NewsViewModel) {
    var currentNav by remember { mutableStateOf(NavDestination.HOME) }

    val userPrefs by viewModel.userPreferences.collectAsState()
    val articles by viewModel.feedArticles.collectAsState()
    val breakingArticles by viewModel.breakingNews.collectAsState()
    val savedArticles by viewModel.savedArticles.collectAsState()
    val savedIds = remember(savedArticles) { savedArticles.map { it.id }.toSet() }

    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedArticle by viewModel.selectedArticle.collectAsState()

    val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()
    val currentAudioId by viewModel.currentAudioArticleId.collectAsState()

    val isIngestionRunning by viewModel.isIngestionRunning.collectAsState()
    val ingestionMsg by viewModel.ingestionBannerMsg.collectAsState()
    val adConfig by viewModel.adConfig.collectAsState()

    val sources by viewModel.allSources.collectAsState()
    val systemLogs by viewModel.systemLogs.collectAsState()

    val showLocationDialog by viewModel.showLocationDialog.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showAdminPanel by viewModel.showAdminPanel.collectAsState()
    val showPrivacyDialog by viewModel.showPrivacyDialog.collectAsState()

    val locationLoading by viewModel.locationLoading.collectAsState()
    val locationMessage by viewModel.locationMessage.collectAsState()

    // Article Detail Screen has topmost priority when selected
    if (selectedArticle != null) {
        val currentArticle = selectedArticle!!
        val related = articles.filter { it.id != currentArticle.id && it.category == currentArticle.category }

        ArticleDetailScreen(
            article = currentArticle,
            currentLang = userPrefs.preferredLanguage,
            isSaved = savedIds.contains(currentArticle.id),
            isPlayingAudio = isPlayingAudio && currentAudioId == currentArticle.id,
            onBack = { viewModel.closeArticle() },
            onToggleSave = { viewModel.toggleSaveArticle(currentArticle.id, savedIds.contains(currentArticle.id)) },
            onToggleAudio = { viewModel.toggleAudio(currentArticle, userPrefs.preferredLanguage) },
            onArticleClick = { viewModel.openArticle(it) },
            relatedArticles = related
        )
        return
    }

    if (showAdminPanel || currentNav == NavDestination.ADMIN) {
        AdminScreen(
            articles = articles,
            sources = sources,
            systemLogs = systemLogs,
            adConfig = adConfig,
            isIngestionRunning = isIngestionRunning,
            onBack = {
                viewModel.setShowAdminPanel(false)
                currentNav = NavDestination.HOME
            },
            onTriggerIngestion = { viewModel.triggerIngestion() },
            onToggleSource = { id, active -> viewModel.toggleSourceActive(id, active) },
            onAddSource = { name, url, category -> viewModel.addCustomSource(name, url, category) },
            onToggleBreaking = { id, breaking -> viewModel.toggleBreakingStatus(id, breaking) },
            onDeleteArticle = { id -> viewModel.deleteArticle(id) },
            onSaveAdConfig = { viewModel.saveAdConfig(it) }
        )
        return
    }

    if (showPrivacyDialog) {
        PrivacyScreen(onBack = { viewModel.setShowPrivacyDialog(false) })
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopHeader(
                    country = userPrefs.country,
                    state = userPrefs.state,
                    city = userPrefs.city,
                    currentLanguageCode = userPrefs.preferredLanguage,
                    isDarkMode = userPrefs.isDarkMode,
                    onLocationClick = { viewModel.setShowLocationDialog(true) },
                    onLanguageClick = { viewModel.setShowLanguageDialog(true) },
                    onSearchClick = { currentNav = NavDestination.SEARCH },
                    onAdminClick = { viewModel.setShowAdminPanel(true) },
                    onThemeToggle = { viewModel.toggleDarkMode(!userPrefs.isDarkMode) }
                )

                // Breaking News Ticker under Header
                BreakingNewsBar(
                    breakingList = breakingArticles,
                    currentLang = userPrefs.preferredLanguage,
                    onArticleClick = { viewModel.openArticle(it) }
                )
            }
        },
        bottomBar = {
            NavigationBar(modifier = Modifier.testTag("bottom_navigation_bar")) {
                NavigationBarItem(
                    selected = currentNav == NavDestination.HOME,
                    onClick = { currentNav = NavDestination.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    modifier = Modifier.testTag(NavDestination.HOME.tag)
                )
                NavigationBarItem(
                    selected = currentNav == NavDestination.SEARCH,
                    onClick = { currentNav = NavDestination.SEARCH },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    modifier = Modifier.testTag(NavDestination.SEARCH.tag)
                )
                NavigationBarItem(
                    selected = currentNav == NavDestination.SAVED,
                    onClick = { currentNav = NavDestination.SAVED },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                    label = { Text("Saved") },
                    modifier = Modifier.testTag(NavDestination.SAVED.tag)
                )
                NavigationBarItem(
                    selected = currentNav == NavDestination.ADMIN,
                    onClick = { currentNav = NavDestination.ADMIN },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                    label = { Text("Console") },
                    modifier = Modifier.testTag(NavDestination.ADMIN.tag)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentNav) {
                NavDestination.HOME -> HomeScreen(
                    articles = articles,
                    savedArticleIds = savedIds,
                    currentLang = userPrefs.preferredLanguage,
                    selectedTab = selectedTab,
                    selectedCategory = selectedCategory,
                    cityName = userPrefs.city,
                    adConfig = adConfig,
                    isPlayingAudio = isPlayingAudio,
                    currentAudioId = currentAudioId,
                    isIngestionRunning = isIngestionRunning,
                    ingestionMsg = ingestionMsg,
                    onSelectTab = { viewModel.selectTab(it) },
                    onSelectCategory = { viewModel.selectCategory(it) },
                    onArticleClick = { viewModel.openArticle(it) },
                    onToggleSave = { viewModel.toggleSaveArticle(it.id, savedIds.contains(it.id)) },
                    onToggleAudio = { viewModel.toggleAudio(it, userPrefs.preferredLanguage) },
                    onTriggerIngestion = { viewModel.triggerIngestion() },
                    onDismissBanner = { viewModel.dismissIngestionBanner() }
                )

                NavDestination.SEARCH -> SearchScreen(
                    query = searchQuery,
                    searchResults = searchResults,
                    savedArticleIds = savedIds,
                    currentLang = userPrefs.preferredLanguage,
                    isPlayingAudio = isPlayingAudio,
                    currentAudioId = currentAudioId,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onArticleClick = { viewModel.openArticle(it) },
                    onToggleSave = { viewModel.toggleSaveArticle(it.id, savedIds.contains(it.id)) },
                    onToggleAudio = { viewModel.toggleAudio(it, userPrefs.preferredLanguage) }
                )

                NavDestination.SAVED -> SavedArticlesScreen(
                    savedArticles = savedArticles,
                    currentLang = userPrefs.preferredLanguage,
                    isPlayingAudio = isPlayingAudio,
                    currentAudioId = currentAudioId,
                    onArticleClick = { viewModel.openArticle(it) },
                    onToggleSave = { viewModel.toggleSaveArticle(it.id, true) },
                    onToggleAudio = { viewModel.toggleAudio(it, userPrefs.preferredLanguage) }
                )

                NavDestination.ADMIN -> {
                    // Handled above
                }
            }
        }
    }

    // Modal Dialogs
    if (showLocationDialog) {
        LocationSelectorDialog(
            currentCountry = userPrefs.country,
            currentState = userPrefs.state,
            currentCity = userPrefs.city,
            isPersonalizationEnabled = userPrefs.isPersonalizationEnabled,
            isLocationLoading = locationLoading,
            locationDetectionMsg = locationMessage,
            onDetectLocation = { viewModel.detectCurrentLocation() },
            onSaveLocation = { country, state, city, enabled ->
                viewModel.setLocation(country, state, city, enabled)
            },
            onDismiss = {
                viewModel.clearLocationMessage()
                viewModel.setShowLocationDialog(false)
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectorDialog(
            selectedLangCode = userPrefs.preferredLanguage,
            onSelectLanguage = { langCode ->
                viewModel.setLanguage(langCode)
            },
            onDismiss = { viewModel.setShowLanguageDialog(false) }
        )
    }
}
