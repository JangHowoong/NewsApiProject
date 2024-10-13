package com.test.newsapiproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "news", indices = [Index(value = ["url"], unique = true)])
data class NewsEntity(
    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "publishedAt")
    val publishedAt: String?,

    @ColumnInfo(name = "content")
    val content: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}