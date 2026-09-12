package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.ArticleSourceRef
import com.example.data.model.LocationTier
import org.json.JSONArray
import org.json.JSONObject

class NewsConverters {
    @TypeConverter
    fun fromLocationTier(tier: LocationTier): String {
        return tier.name
    }

    @TypeConverter
    fun toLocationTier(value: String): LocationTier {
        return try {
            LocationTier.valueOf(value)
        } catch (e: Exception) {
            LocationTier.LOCAL
        }
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        val array = JSONArray()
        list.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromSourceRefList(list: List<ArticleSourceRef>): String {
        val array = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("sourceName", it.sourceName)
            obj.put("sourceUrl", it.sourceUrl)
            obj.put("articleUrl", it.articleUrl)
            obj.put("licenseStatus", it.licenseStatus)
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toSourceRefList(value: String): List<ArticleSourceRef> {
        if (value.isBlank()) return emptyList()
        return try {
            val array = JSONArray(value)
            val list = mutableListOf<ArticleSourceRef>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ArticleSourceRef(
                        sourceName = obj.optString("sourceName", "News Wire"),
                        sourceUrl = obj.optString("sourceUrl", ""),
                        articleUrl = obj.optString("articleUrl", ""),
                        licenseStatus = obj.optString("licenseStatus", "Open RSS")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
