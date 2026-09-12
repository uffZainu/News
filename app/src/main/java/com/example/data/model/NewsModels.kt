package com.example.data.model

data class Article(
    val id: String,
    val title: String,
    val slug: String,
    val summary: String,
    val fullContent: String,
    val category: String,
    val importanceScore: Int = 5, // 1 to 10
    val isBreaking: Boolean = false,
    val isFactChecked: Boolean = true,
    val factCheckNotes: String = "Multi-source verified against official public releases.",
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
)

data class ArticleSourceRef(
    val sourceName: String,
    val sourceUrl: String,
    val articleUrl: String,
    val licenseStatus: String = "Licensed Open Feed"
)

enum class LocationTier {
    LOCAL,
    STATE,
    NATIONAL,
    WORLD
}

data class NewsSource(
    val id: String,
    val name: String,
    val websiteUrl: String,
    val feedUrl: String,
    val licenseStatus: String, // "Open RSS", "Public Domain", "Licensed Media Wire"
    val category: String,
    val country: String,
    val language: String = "en",
    val isActive: Boolean = true,
    val lastFetchedAt: Long = 0L,
    val fetchStatus: String = "OK"
)

data class UserLocation(
    val country: String = "India",
    val state: String = "West Bengal",
    val district: String = "Kolkata",
    val city: String = "Kolkata",
    val isPersonalizationEnabled: Boolean = true,
    val isGpsActive: Boolean = false
)

data class Language(
    val code: String,
    val name: String,
    val nativeName: String
)

data class AdConfig(
    val isHeaderAdEnabled: Boolean = true,
    val isInFeedAdEnabled: Boolean = true,
    val isArticleAdEnabled: Boolean = true,
    val sponsorName: String = "Novyra Ad Network / Google AdMob Partner",
    val customNotice: String = "Advertisement • Distinguishable editorial sponsor",
    val adMobAppId: String = "ca-app-pub-6600948809833153~8497944217",
    val adMobBannerUnitId: String = "ca-app-pub-6600948809833153/1470693401"
)

data class SystemLog(
    val id: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val level: String = "INFO",
    val tag: String,
    val message: String
)
