package com.test.newsapiproject.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsItemDao(): NewsItemDao

    companion object {
        const val DATABASE_NAME = "news_database"
    }
}