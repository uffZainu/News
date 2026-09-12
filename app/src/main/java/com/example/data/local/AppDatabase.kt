package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ArticleEntity::class,
        SourceEntity::class,
        SavedArticleEntity::class,
        SystemLogEntity::class,
        AdConfigEntity::class,
        UserPreferencesEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(NewsConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun sourceDao(): SourceDao
    abstract fun savedArticleDao(): SavedArticleDao
    abstract fun systemLogDao(): SystemLogDao
    abstract fun adConfigDao(): AdConfigDao
    abstract fun userPrefDao(): UserPrefDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "novyra_news_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
