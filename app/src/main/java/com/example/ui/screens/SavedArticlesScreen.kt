package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.engine.LanguageTranslationService
import com.example.data.model.Article
import com.example.ui.components.StandardArticleCard

@Composable
fun SavedArticlesScreen(
    savedArticles: List<Article>,
    currentLang: String,
    isPlayingAudio: Boolean,
    currentAudioId: String?,
    onArticleClick: (Article) -> Unit,
    onToggleSave: (Article) -> Unit,
    onToggleAudio: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("saved_articles_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = LanguageTranslationService.getString("offline_reading", currentLang),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Articles saved on your device are accessible anytime without internet connection.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (savedArticles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = LanguageTranslationService.getString("no_saved_articles", currentLang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedArticles, key = { it.id }) { article ->
                    StandardArticleCard(
                        article = article,
                        currentLang = currentLang,
                        isSaved = true,
                        isPlayingAudio = isPlayingAudio && currentAudioId == article.id,
                        onClick = { onArticleClick(article) },
                        onToggleSave = { onToggleSave(article) },
                        onToggleAudio = { onToggleAudio(article) }
                    )
                }
            }
        }
    }
}
