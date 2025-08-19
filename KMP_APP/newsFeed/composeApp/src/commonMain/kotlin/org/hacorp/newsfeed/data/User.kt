package org.hacorp.newsfeed.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImageUrl: String? = null,
    val preferences: UserPreferences = UserPreferences()
)

@Serializable
data class UserPreferences(
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val preferredCategories: List<String> = emptyList()
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val user: User,
    val token: String,
    val success: Boolean,
    val message: String? = null
)
