package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.LanguageTranslationService
import com.example.data.model.LocationTier
import com.example.data.seed.InitialDataSeed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Novyra News", appName)
    }

    @Test
    fun `initial data contains local state national and world articles`() {
        val articles = InitialDataSeed.articles
        assertTrue(articles.isNotEmpty())

        val localArticles = articles.filter { it.locationTier == LocationTier.LOCAL }
        val stateArticles = articles.filter { it.locationTier == LocationTier.STATE }
        val nationalArticles = articles.filter { it.locationTier == LocationTier.NATIONAL }
        val worldArticles = articles.filter { it.locationTier == LocationTier.WORLD }

        assertTrue("Should have local articles", localArticles.isNotEmpty())
        assertTrue("Should have state articles", stateArticles.isNotEmpty())
        assertTrue("Should have national articles", nationalArticles.isNotEmpty())
        assertTrue("Should have world articles", worldArticles.isNotEmpty())

        // Check breaking news article exists
        val breaking = articles.filter { it.isBreaking }
        assertTrue("Should have at least one breaking news article", breaking.isNotEmpty())
    }

    @Test
    fun `multilingual translation service provides valid strings`() {
        val enTagline = LanguageTranslationService.getString("app_tagline", "en")
        assertEquals("Local to Global. One Place.", enTagline)

        val hiTagline = LanguageTranslationService.getString("app_tagline", "hi")
        assertTrue(hiTagline.contains("स्थान"))

        val bnTagline = LanguageTranslationService.getString("app_tagline", "bn")
        assertTrue(bnTagline.contains("স্থান") || bnTagline.contains("ঠিকানা"))
    }

    @Test
    fun `novyra location manager initializes properly and checks permissions`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val locationManager = com.example.data.location.NovyraLocationManager(context)
        assertNotNull(locationManager)
        // In clean robolectric environment without granted permissions, hasLocationPermission defaults to false
        assertFalse(locationManager.hasLocationPermission())
    }

    @Test
    fun `admob configuration matches user supplied app id and unit id`() {
        val adConfig = com.example.data.model.AdConfig()
        assertEquals("ca-app-pub-6600948809833153~8497944217", adConfig.adMobAppId)
        assertEquals("ca-app-pub-6600948809833153/1470693401", adConfig.adMobBannerUnitId)
        assertEquals("ca-app-pub-6600948809833153~8497944217", com.example.ui.components.NovyraAdMobManager.DEFAULT_APP_ID)
        assertEquals("ca-app-pub-6600948809833153/1470693401", com.example.ui.components.NovyraAdMobManager.DEFAULT_BANNER_UNIT_ID)
    }
}
