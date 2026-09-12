package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Article
import com.example.data.model.ArticleSourceRef
import com.example.data.model.LocationTier
import com.example.data.model.NewsSource

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val slug: String,
    val summary: String,
    val fullContent: String,
    val category: String,
    val importanceScore: Int = 5,
    val isBreaking: Boolean = false,
    val isFactChecked: Boolean = true,
    val factCheckNotes: String = "",
    val publishedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val country: String = "India",
    val state: String = "West Bengal",
    val district: String = "Kolkata",
    val city: String = "Kolkata",
    val locationTier: LocationTier = LocationTier.LOCAL,
    val primarySourceName: String,
    val primarySourceUrl: String,
    val originalArticleUrl: String,
    val clusterSources: List<ArticleSourceRef> = emptyList(),
    val imageUrl: String = "",
    val imageCaption: String = "",
    val tags: List<String> = emptyList(),
    val language: String = "en",
    val verifiedAiSummary: Boolean = true,
    val readTimeMinutes: Int = 3,
    val viewsCount: Int = 0,
    val isSensitive: Boolean = false,
    val sensitivityNotice: String = ""
) {
    fun toDomain(): Article = Article(
        id = id,
        title = title,
        slug = slug,
        summary = summary,
        fullContent = fullContent,
        category = category,
        importanceScore = importanceScore,
        isBreaking = isBreaking,
        isFactChecked = isFactChecked,
        factCheckNotes = factCheckNotes,
        publishedAt = publishedAt,
        updatedAt = updatedAt,
        country = country,
        state = state,
        district = district,
        city = city,
        locationTier = locationTier,
        primarySourceName = primarySourceName,
        primarySourceUrl = primarySourceUrl,
        originalArticleUrl = originalArticleUrl,
        clusterSources = clusterSources,
        imageUrl = imageUrl,
        imageCaption = imageCaption,
        tags = tags,
        language = language,
        verifiedAiSummary = verifiedAiSummary,
        readTimeMinutes = readTimeMinutes,
        viewsCount = viewsCount,
        isSensitive = isSensitive,
        sensitivityNotice = sensitivityNotice
    )

    companion object {
        fun fromDomain(article: Article): ArticleEntity = ArticleEntity(
            id = article.id,
            title = article.title,
            slug = article.slug,
            summary = article.summary,
            fullContent = article.fullContent,
            category = article.category,
            importanceScore = article.importanceScore,
            isBreaking = article.isBreaking,
            isFactChecked = article.isFactChecked,
            factCheckNotes = article.factCheckNotes,
            publishedAt = article.publishedAt,
            updatedAt = article.updatedAt,
            country = article.country,
            state = article.state,
            district = article.district,
            city = article.city,
            locationTier = article.locationTier,
            primarySourceName = article.primarySourceName,
            primarySourceUrl = article.primarySourceUrl,
            originalArticleUrl = article.originalArticleUrl,
            clusterSources = article.clusterSources,
            imageUrl = article.imageUrl,
            imageCaption = article.imageCaption,
            tags = article.tags,
            language = article.language,
            verifiedAiSummary = article.verifiedAiSummary,
            readTimeMinutes = article.readTimeMinutes,
            viewsCount = article.viewsCount,
            isSensitive = article.isSensitive,
            sensitivityNotice = article.sensitivityNotice
        )
    }
}

@Entity(tableName = "news_sources")
data class SourceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val websiteUrl: String,
    val feedUrl: String,
    val licenseStatus: String,
    val category: String,
    val country: String,
    val language: String = "en",
    val isActive: Boolean = true,
    val lastFetchedAt: Long = 0L,
    val fetchStatus: String = "OK"
) {
    fun toDomain(): NewsSource = NewsSource(
        id = id,
        name = name,
        websiteUrl = websiteUrl,
        feedUrl = feedUrl,
        licenseStatus = licenseStatus,
        category = category,
        country = country,
        language = language,
        isActive = isActive,
        lastFetchedAt = lastFetchedAt,
        fetchStatus = fetchStatus
    )

    companion object {
        fun fromDomain(source: NewsSource): SourceEntity = SourceEntity(
            id = source.id,
            name = source.name,
            websiteUrl = source.websiteUrl,
            feedUrl = source.feedUrl,
            licenseStatus = source.licenseStatus,
            category = source.category,
            country = source.country,
            language = source.language,
            isActive = source.isActive,
            lastFetchedAt = source.lastFetchedAt,
            fetchStatus = source.fetchStatus
        )
    }
}

@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey
    val articleId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "system_logs")
data class SystemLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val level: String = "INFO",
    val tag: String,
    val message: String
)

@Entity(tableName = "ad_configs")
data class AdConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val isHeaderAdEnabled: Boolean = true,
    val isInFeedAdEnabled: Boolean = true,
    val isArticleAdEnabled: Boolean = true,
    val sponsorName: String = "Novyra Ad Network / Google AdMob Partner",
    val customNotice: String = "Advertisement • Clearly distinguished sponsor",
    val adMobAppId: String = "ca-app-pub-6600948809833153~8497944217",
    val adMobBannerUnitId: String = "ca-app-pub-6600948809833153/1470693401"
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Int = 1,
    val country: String = "India",
    val state: String = "West Bengal",
    val district: String = "Kolkata",
    val city: String = "Kolkata",
    val isPersonalizationEnabled: Boolean = true,
    val preferredLanguage: String = "en",
    val isDarkMode: Boolean = false
)
