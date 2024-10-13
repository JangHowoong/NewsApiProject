package com.test.newsapiproject.network.api

import com.test.newsapiproject.Constants
import com.test.newsapiproject.network.data.NewsInfo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.GET_HEADLINES)
    suspend fun getTestPost(): Response<NewsInfo>

}