package com.test.newsapiproject.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.newsapiproject.Application
import com.test.newsapiproject.network.data.Article
import com.test.newsapiproject.room.NewsEntity
import com.test.newsapiproject.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val repository: NewsRepository
) : AndroidViewModel(application) {

    companion object {
        private val TAG = NewsViewModel::class.java.simpleName
    }

    private val newsItemFlow = MutableSharedFlow<List<Article>>(replay = 1)
    val newItemLiveData = newsItemFlow.asLiveData()

    fun fetchApiData() {
        viewModelScope.launch {
            try {
                repository.getRetrofitService().let { response ->
                    if(response.isSuccessful) {
                        // 성공
                        Log.d(TAG, "onResponse")
                        val body = response.body()

                        if(body != null) {
                            val item = body.articles
                            syncPostsWithDatabase(item)
                        }
                    }else{
                        // 실패
                        Log.d(TAG, "onFailure : ${response.message()}")
                        getRoomDatabaseItems()
                    }
                }
            }catch (e: Exception) {
                // 예외
                Log.d(TAG, "fetchApiData: ${e.message}")
                getRoomDatabaseItems()
            }
        }
    }

    private fun syncPostsWithDatabase(articles: List<Article>) {
        viewModelScope.launch(Dispatchers.IO) {

            val getPosts = repository.getPosts()
            val newArticles = articles.filter {
                it.imageUrl != null
            }.map {
                NewsEntity(
                    author = it.author,
                    title = it.title,
                    description = it.description,
                    url = it.url,
                    imageUrl = it.imageUrl,
                    publishedAt = it.publishedAt,
                    content = it.content
                )
            }

            // 불필요한 요청을 방지하기 위해 로컬 데이터와 일치하는지 확인
            val isSameData = getPosts.size == newArticles.size &&
                    getPosts.zip(newArticles).all { (getPosts, newArticles) ->
                        getPosts.url == newArticles.url
                    }

            if(isSameData) {
                getRoomDatabaseItems()
            }else{
                newsItemFlow.emit(articles)

                repository.deleteAll()
                repository.insertPosts(newArticles)
            }
        }
    }

    private fun getRoomDatabaseItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = repository.getPosts()

            if(posts.isEmpty()) {
                return@launch
            }

            val newArticles = posts.map {
                Article(
                    source = null,
                    author = it.author,
                    title = it.title,
                    description = it.description,
                    url = it.url,
                    imageUrl = it.imageUrl,
                    publishedAt = it.publishedAt,
                    content = it.content
                )
            }
            newsItemFlow.emit(newArticles)
        }
    }

}