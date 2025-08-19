package org.hacorp.newsfeed.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String? = null,
    val author: String,
    val publishedAt: String, // ISO 8601 format
    val source: String,
    val category: String = "General",
    val url: String? = null
)

@Serializable
data class NewsResponse(
    val articles: List<NewsArticle>,
    val totalResults: Int,
    val status: String
)
