package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.LanguageTranslationService
import com.example.data.model.Article
import com.example.ui.theme.NovyraBreakingRed

@Composable
fun BreakingNewsBar(
    breakingList: List<Article>,
    currentLang: String,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val topBreaking = breakingList.firstOrNull()

    AnimatedVisibility(
        visible = topBreaking != null,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        if (topBreaking != null) {
            val translatedHeadline = LanguageTranslationService.translateHeadline(topBreaking.title, currentLang)

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(NovyraBreakingRed.copy(alpha = 0.12f))
                    .clickable { onArticleClick(topBreaking) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("breaking_news_ticker_bar"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NovyraBreakingRed)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "BREAKING",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = translatedHeadline,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Read Breaking News",
                    tint = NovyraBreakingRed,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
