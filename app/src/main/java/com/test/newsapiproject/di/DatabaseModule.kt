package com.test.newsapiproject.di

import android.content.Context
import androidx.room.Room
import com.test.newsapiproject.room.NewsDatabase
import com.test.newsapiproject.room.NewsItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = NewsDatabase::class.java,
            name = NewsDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsItemDao(database: NewsDatabase): NewsItemDao {
        return database.newsItemDao()
    }

}