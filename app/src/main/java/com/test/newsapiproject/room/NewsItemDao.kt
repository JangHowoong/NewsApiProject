package com.test.newsapiproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsEntity>)

    @Query("SELECT * FROM news")
    suspend fun getArticles(): List<NewsEntity>

    @Query("DELETE FROM news")
    suspend fun deleteAll()
}