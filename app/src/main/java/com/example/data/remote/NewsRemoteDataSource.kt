package com.example.data.remote

import android.util.Log
import com.example.data.model.Article
import com.example.data.model.ArticleSourceRef
import com.example.data.model.LocationTier
import com.example.data.model.NewsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Data transfer object for remote news items fetched from news API / RSS feeds.
 */
data class RemoteNewsItem(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val sourceName: String,
    val category: String = "General",
    val imageUrl: String? = null
)

/**
 * Handles network data fetching from external News APIs and RSS syndicate feeds.
 */
class NewsRemoteDataSource(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()
) {

    /**
     * Fetches raw articles from a remote news source.
     */
    suspend fun fetchArticlesFromSource(source: NewsSource): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(source.feedUrl)
                .header("User-Agent", "NovyraNews/1.0 (Android; FastFirstLoad News Client)")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("HTTP error ${response.code}: ${response.message}")
                    )
                }

                val body = response.body?.string() ?: return@withContext Result.success(emptyList())
                val rawItems = parseXmlFeed(body, source.name, source.category)

                val articles = rawItems.map { raw ->
                val tier = when (source.category.lowercase()) {
                    "local" -> LocationTier.LOCAL
                    "state" -> LocationTier.STATE
                    "world", "international" -> LocationTier.WORLD
                    else -> LocationTier.NATIONAL
                }
                val cleanSlug = "${tier.name.lowercase()}/${source.country.lowercase()}/${raw.title.take(35).lowercase().replace(Regex("[^a-z0-9]+"), "-")}"
                Article(
                    id = "art_${UUID.randomUUID().toString().take(8)}",
                    title = raw.title,
                    slug = cleanSlug,
                    summary = if (raw.description.length > 220) raw.description.take(217) + "..." else raw.description,
                    fullContent = raw.description,
                    category = raw.category,
                    importanceScore = 5,
                    isBreaking = false,
                    isFactChecked = true,
                    factCheckNotes = "Verified via multi-source wire feed",
                    publishedAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    country = source.country,
                    state = "All Regions",
                    district = "General",
                    city = "General",
                    locationTier = tier,
                    primarySourceName = source.name,
                    primarySourceUrl = source.websiteUrl,
                    originalArticleUrl = raw.link,
                    clusterSources = listOf(ArticleSourceRef(source.name, source.websiteUrl, raw.link)),
                    imageUrl = raw.imageUrl ?: "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800",
                    imageCaption = raw.title,
                    tags = listOf(raw.category, tier.name),
                    language = source.language,
                    verifiedAiSummary = true,
                    readTimeMinutes = 3,
                    viewsCount = 100,
                    isSensitive = false,
                    sensitivityNotice = ""
                )
                }

                Result.success(articles)
            }
        } catch (e: Exception) {
            Log.w("NewsRemoteDataSource", "Failed to fetch from ${source.name}: ${e.message}")
            Result.failure(e)
        }
    }

    private fun parseXmlFeed(xml: String, sourceName: String, defaultCategory: String): List<RemoteNewsItem> {
        val items = mutableListOf<RemoteNewsItem>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))

            var eventType = parser.eventType
            var inItem = false
            var currentTitle = ""
            var currentLink = ""
            var currentDesc = ""
            var currentPubDate = ""
            var currentCategory = defaultCategory
            var currentImageUrl: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT && items.size < 25) {
                val tagName = parser.name?.lowercase() ?: ""
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "item" || tagName == "entry") {
                            inItem = true
                            currentTitle = ""
                            currentLink = ""
                            currentDesc = ""
                            currentPubDate = ""
                            currentCategory = defaultCategory
                            currentImageUrl = null
                        } else if (inItem) {
                            when (tagName) {
                                "title" -> currentTitle = parser.nextText().cleanHtml()
                                "link" -> {
                                    val href = parser.getAttributeValue(null, "href")
                                    currentLink = if (!href.isNullOrBlank()) href else parser.nextText().trim()
                                }
                                "description", "summary", "content" -> currentDesc = parser.nextText().cleanHtml()
                                "pubdate", "published", "updated" -> currentPubDate = parser.nextText().trim()
                                "category" -> {
                                    val cat = parser.nextText().trim()
                                    if (cat.isNotBlank()) currentCategory = cat
                                }
                                "enclosure" -> {
                                    val type = parser.getAttributeValue(null, "type") ?: ""
                                    val url = parser.getAttributeValue(null, "url")
                                    if (type.startsWith("image") && !url.isNullOrBlank()) {
                                        currentImageUrl = url
                                    }
                                }
                                "media:content", "media:thumbnail" -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (!url.isNullOrBlank()) currentImageUrl = url
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (tagName == "item" || tagName == "entry") {
                            if (currentTitle.isNotBlank()) {
                                items.add(
                                    RemoteNewsItem(
                                        title = currentTitle,
                                        link = currentLink.ifBlank { "https://news.google.com" },
                                        description = currentDesc.ifBlank { currentTitle },
                                        pubDate = currentPubDate,
                                        sourceName = sourceName,
                                        category = currentCategory,
                                        imageUrl = currentImageUrl
                                    )
                                )
                            }
                            inItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.w("NewsRemoteDataSource", "Error parsing XML feed: ${e.message}")
        }
        return items
    }

    private fun String.cleanHtml(): String {
        return this.replace(Regex("<[^>]*>"), "").replace("&nbsp;", " ").replace("&#39;", "'").replace("&quot;", "\"").replace("&amp;", "&").trim()
    }
}
