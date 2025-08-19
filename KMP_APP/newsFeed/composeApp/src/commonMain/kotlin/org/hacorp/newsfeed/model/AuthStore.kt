package org.hacorp.newsfeed.model

import org.hacorp.newsfeed.data.User
import org.hacorp.newsfeed.store_lib.InMemoryStore

class AuthStore : InMemoryStore<AuthIntent, AuthState>(
    initialState = AuthState.NotAuthenticated
) {
    override fun handleIntent(intent: AuthIntent): AuthState {
        return when (intent) {
            is AuthIntent.Login -> {
                // In a real app, this would make an API call
                // For demo purposes, we'll simulate a successful login
                if (intent.email.isNotEmpty() && intent.password.isNotEmpty()) {
                    AuthState.Authenticated(
                        user = User(
                            id = "1",
                            email = intent.email,
                            name = intent.email.substringBefore("@").replaceFirstChar { it.uppercase() }
                        ),
//                        token = "demo_token_${System.currentTimeMillis()}"
                        token = "demo_token"
                    )
                } else {
                    AuthState.Error("Please enter valid credentials")
                }
            }
            AuthIntent.Logout -> AuthState.NotAuthenticated
            AuthIntent.ClearError -> when (val currentState = stateFlow.value) {
                is AuthState.Error -> AuthState.NotAuthenticated
                else -> currentState
            }
            is AuthIntent.LoginStart -> AuthState.Loading
        }
    }
}

sealed interface AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent
    object LoginStart : AuthIntent
    object Logout : AuthIntent
    object ClearError : AuthIntent
}

sealed interface AuthState {
    object NotAuthenticated : AuthState
    object Loading : AuthState
    data class Authenticated(val user: User, val token: String) : AuthState
    data class Error(val message: String) : AuthState
}
