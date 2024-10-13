package com.test.newsapiproject.di

import com.test.newsapiproject.network.api.ApiService
import com.test.newsapiproject.room.NewsItemDao
import com.test.newsapiproject.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService, newsItemDao: NewsItemDao): NewsRepository {
        return NewsRepository(apiService, newsItemDao)
    }

}