package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.LanguageTranslationService
import com.example.data.model.AdConfig
import com.example.data.model.Article
import com.example.ui.components.AdBannerCard
import com.example.ui.components.HeroArticleCard
import com.example.ui.components.StandardArticleCard
import com.example.ui.theme.NovyraGoldAccent
import com.example.ui.theme.NovyraNavyPrimary
import com.example.ui.viewmodel.LocationTab

val CATEGORIES = listOf(
    "All",
    "Local",
    "Politics",
    "Technology",
    "Science",
    "Finance",
    "Jobs & Careers",
    "Health",
    "Sports",
    "Environment",
    "Accidents / Emergencies",
    "Trending"
)

@Composable
fun HomeScreen(
    articles: List<Article>,
    savedArticleIds: Set<String>,
    currentLang: String,
    selectedTab: LocationTab,
    selectedCategory: String,
    cityName: String,
    adConfig: AdConfig,
    isPlayingAudio: Boolean,
    currentAudioId: String?,
    isIngestionRunning: Boolean,
    ingestionMsg: String?,
    onSelectTab: (LocationTab) -> Unit,
    onSelectCategory: (String) -> Unit,
    onArticleClick: (Article) -> Unit,
    onToggleSave: (Article) -> Unit,
    onToggleAudio: (Article) -> Unit,
    onTriggerIngestion: () -> Unit,
    onDismissBanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Ingestion Banner notification if any
        if (ingestionMsg != null) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NovyraGoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = ingestionMsg,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = onDismissBanner, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Location Hierarchy Tabs: Top Stories, Local, State, National, World, Trending
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = NovyraGoldAccent,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("location_tier_tabs")
        ) {
            LocationTab.values().forEach { tab ->
                val label = if (tab == LocationTab.LOCAL) "📍 $cityName" else "${tab.icon} ${tab.label}"
                Tab(
                    selected = selectedTab == tab,
                    onClick = { onSelectTab(tab) },
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CATEGORIES.forEach { category ->
                val isSelected = selectedCategory.equals(category, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category) },
                    label = {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NovyraNavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("category_chip_$category")
                )
            }
        }

        // Main Feed List
        if (articles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No stories found in this category.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    IconButton(
                        onClick = onTriggerIngestion,
                        enabled = !isIngestionRunning,
                        modifier = Modifier.testTag("refresh_empty_feed_button")
                    ) {
                        if (isIngestionRunning) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val heroArticle = articles.firstOrNull()

                // Hero Story Card
                if (heroArticle != null) {
                    item(key = "hero_${heroArticle.id}") {
                        HeroArticleCard(
                            article = heroArticle,
                            currentLang = currentLang,
                            isSaved = savedArticleIds.contains(heroArticle.id),
                            isPlayingAudio = isPlayingAudio && currentAudioId == heroArticle.id,
                            onClick = { onArticleClick(heroArticle) },
                            onToggleSave = { onToggleSave(heroArticle) },
                            onToggleAudio = { onToggleAudio(heroArticle) }
                        )
                    }
                }

                // In-feed Ad Placement after hero card (if enabled)
                if (adConfig.isInFeedAdEnabled) {
                    item(key = "in_feed_ad") {
                        AdBannerCard(
                            sponsorName = adConfig.sponsorName,
                            customNotice = adConfig.customNotice,
                            adUnitId = adConfig.adMobBannerUnitId
                        )
                    }
                }

                // Remaining Standard Articles
                val remainingArticles = if (articles.size > 1) articles.subList(1, articles.size) else emptyList()

                itemsIndexed(
                    items = remainingArticles,
                    key = { _, item -> item.id }
                ) { index, article ->
                    StandardArticleCard(
                        article = article,
                        currentLang = currentLang,
                        isSaved = savedArticleIds.contains(article.id),
                        isPlayingAudio = isPlayingAudio && currentAudioId == article.id,
                        onClick = { onArticleClick(article) },
                        onToggleSave = { onToggleSave(article) },
                        onToggleAudio = { onToggleAudio(article) }
                    )
                }

                // Bottom padding
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}
