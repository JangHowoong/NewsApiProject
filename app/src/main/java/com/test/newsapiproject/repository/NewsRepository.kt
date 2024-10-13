package com.test.newsapiproject.repository

import com.test.newsapiproject.network.api.ApiService
import com.test.newsapiproject.network.data.NewsInfo
import com.test.newsapiproject.room.NewsEntity
import com.test.newsapiproject.room.NewsItemDao
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val apiService: ApiService,
    private val newsItemDao: NewsItemDao
) {

    // Retrofit
    suspend fun getRetrofitService(): Response<NewsInfo> {
        return apiService.getTestPost()
    }

    // Room Database
    suspend fun insertPosts(articles: List<NewsEntity>) {
        newsItemDao.insertArticles(articles)
    }

    suspend fun getPosts(): List<NewsEntity> {
        return newsItemDao.getArticles()
    }

    suspend fun deleteAll() {
        newsItemDao.deleteAll()
    }
}