package com.test.newsapiproject

class Constants {

    companion object {
        private const val API_KEY = BuildConfig.API_KEY

        const val BASE_URL = "https://newsapi.org/v2/"
        const val GET_HEADLINES = "top-headlines?country=us&apiKey=$API_KEY"
    }

}