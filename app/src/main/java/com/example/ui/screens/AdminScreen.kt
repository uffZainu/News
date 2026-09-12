package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdConfig
import com.example.data.model.Article
import com.example.data.model.NewsSource
import com.example.data.model.SystemLog
import com.example.ui.theme.NovyraBreakingRed
import com.example.ui.theme.NovyraGoldAccent
import com.example.ui.theme.NovyraNavyDark
import com.example.ui.theme.NovyraVerifiedGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    articles: List<Article>,
    sources: List<NewsSource>,
    systemLogs: List<SystemLog>,
    adConfig: AdConfig,
    isIngestionRunning: Boolean,
    onBack: () -> Unit,
    onTriggerIngestion: () -> Unit,
    onToggleSource: (String, Boolean) -> Unit,
    onAddSource: (String, String, String) -> Unit,
    onToggleBreaking: (String, Boolean) -> Unit,
    onDeleteArticle: (String) -> Unit,
    onSaveAdConfig: (AdConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showAddSourceDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = NovyraGoldAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin & Ingestion Console",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("admin_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs: Overview, Sources, Stories, Ads, Logs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.testTag("admin_subtabs")
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Overview") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Feeds (${sources.size})") }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("Editorial") }
                )
                Tab(
                    selected = selectedTabIndex == 3,
                    onClick = { selectedTabIndex = 3 },
                    text = { Text("Ad Monetization") }
                )
                Tab(
                    selected = selectedTabIndex == 4,
                    onClick = { selectedTabIndex = 4 },
                    text = { Text("Audit Logs") }
                )
            }

            when (selectedTabIndex) {
                0 -> OverviewTab(
                    articlesCount = articles.size,
                    sourcesCount = sources.size,
                    breakingCount = articles.count { it.isBreaking },
                    isIngestionRunning = isIngestionRunning,
                    onTriggerIngestion = onTriggerIngestion
                )
                1 -> SourcesTab(
                    sources = sources,
                    onToggleSource = onToggleSource,
                    onAddNewSource = { showAddSourceDialog = true }
                )
                2 -> EditorialTab(
                    articles = articles,
                    onToggleBreaking = onToggleBreaking,
                    onDeleteArticle = onDeleteArticle
                )
                3 -> MonetizationTab(
                    adConfig = adConfig,
                    onSaveAdConfig = onSaveAdConfig
                )
                4 -> LogsTab(systemLogs = systemLogs)
            }
        }
    }

    if (showAddSourceDialog) {
        AddSourceModal(
            onDismiss = { showAddSourceDialog = false },
            onAdd = { name, url, category ->
                onAddSource(name, url, category)
                showAddSourceDialog = false
            }
        )
    }
}

@Composable
private fun OverviewTab(
    articlesCount: Int,
    sourcesCount: Int,
    breakingCount: Int,
    isIngestionRunning: Boolean,
    onTriggerIngestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SYSTEM STATUS & METRICS",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = NovyraGoldAccent
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiCard(
                label = "Total Articles",
                value = articlesCount.toString(),
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                label = "Active Feeds",
                value = sourcesCount.toString(),
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                label = "Breaking News",
                value = breakingCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Trigger Ingestion Action Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NovyraGoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Automated News Pipeline Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Runs the automated pipeline: fetches legal RSS/open wire feeds, executes deduplication clustering, validates sensitivity, extracts location metadata, and runs AI summaries.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onTriggerIngestion,
                    enabled = !isIngestionRunning,
                    colors = ButtonDefaults.buttonColors(containerColor = NovyraNavyDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_run_pipeline_button")
                ) {
                    if (isIngestionRunning) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Processing Feeds & AI Summaries...")
                    } else {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = NovyraGoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Execute Ingestion Pipeline Now", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = NovyraNavyDark)
        }
    }
}

@Composable
private fun SourcesTab(
    sources: List<NewsSource>,
    onToggleSource: (String, Boolean) -> Unit,
    onAddNewSource: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LICENSED & OPEN NEWS SOURCES",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = NovyraGoldAccent
            )
            Button(
                onClick = onAddNewSource,
                modifier = Modifier.testTag("admin_add_feed_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Feed")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sources, key = { it.id }) { source ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = source.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "License: ${source.licenseStatus} • ${source.category}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Status: ${source.fetchStatus}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = NovyraVerifiedGreen
                            )
                        }

                        Switch(
                            checked = source.isActive,
                            onCheckedChange = { onToggleSource(source.id, it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorialTab(
    articles: List<Article>,
    onToggleBreaking: (String, Boolean) -> Unit,
    onDeleteArticle: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles, key = { it.id }) { article ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: ${article.city}, ${article.state} • Score: ${article.importanceScore}/10",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = article.isBreaking,
                                onCheckedChange = { onToggleBreaking(article.id, it) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (article.isBreaking) "BREAKING" else "Standard",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (article.isBreaking) NovyraBreakingRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        IconButton(onClick = { onDeleteArticle(article.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonetizationTab(
    adConfig: AdConfig,
    onSaveAdConfig: (AdConfig) -> Unit
) {
    var inFeed by remember { mutableStateOf(adConfig.isInFeedAdEnabled) }
    var header by remember { mutableStateOf(adConfig.isHeaderAdEnabled) }
    var sponsor by remember { mutableStateOf(adConfig.sponsorName) }
    var adMobAppId by remember { mutableStateOf(adConfig.adMobAppId) }
    var adMobUnitId by remember { mutableStateOf(adConfig.adMobBannerUnitId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "GOOGLE ADMOB & MONETIZATION SETTINGS",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = NovyraGoldAccent
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "In-Feed AdMob Banners", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = inFeed, onCheckedChange = { inFeed = it })
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Article Sponsor Badges", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = header, onCheckedChange = { header = it })
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = adMobAppId,
                    onValueChange = { adMobAppId = it },
                    label = { Text("Google AdMob App ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = adMobUnitId,
                    onValueChange = { adMobUnitId = it },
                    label = { Text("Banner Ad Unit ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sponsor,
                    onValueChange = { sponsor = it },
                    label = { Text("Sponsor Network Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onSaveAdConfig(
                            adConfig.copy(
                                isInFeedAdEnabled = inFeed,
                                isHeaderAdEnabled = header,
                                sponsorName = sponsor,
                                adMobAppId = adMobAppId,
                                adMobBannerUnitId = adMobUnitId
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save AdMob & Ad Settings")
                }
            }
        }
    }
}

@Composable
private fun LogsTab(systemLogs: List<SystemLog>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(systemLogs) { log ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "[${log.level}] ${log.tag}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (log.level == "WARN") NovyraBreakingRed else NovyraNavyDark
                        )
                        Text(
                            text = "${log.timestamp % 100000} ms",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = log.message, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun AddSourceModal(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Technology") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add News RSS Feed") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Source Publisher Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("RSS / Atom Feed URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank() && url.isNotBlank()) onAdd(name, url, category) },
                enabled = name.isNotBlank() && url.isNotBlank()
            ) {
                Text("Add Feed")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
