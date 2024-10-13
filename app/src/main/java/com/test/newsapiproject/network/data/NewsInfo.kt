package com.test.newsapiproject.network.data

import com.google.gson.annotations.SerializedName

data class NewsInfo(
    @SerializedName("status")
    val status: String?,

    @SerializedName("totalResults")
    val totalResults: Int?,

    @SerializedName("articles")
    val articles: List<Article>
)

data class Article(
    @SerializedName("source")
    val source: Source?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val imageUrl: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
) {
    data class Source(
        @SerializedName("id")
        val id: String?,

        @SerializedName("name")
        val name: String?
    )
}