package org.hacorp.newsfeed.model

import org.hacorp.newsfeed.store_lib.InMemoryStore

class ThemeStore : InMemoryStore<ThemeIntent, ThemeState>(
    initialState = ThemeState(useDarkTheme = DarkThemeState.SYSTEM)
) {
    override fun handleIntent(intent: ThemeIntent): ThemeState {
        return when (intent) {
            ThemeIntent.SetSystemIntent -> ThemeState(useDarkTheme = DarkThemeState.SYSTEM)
            ThemeIntent.SetLightIntent -> ThemeState(useDarkTheme = DarkThemeState.LIGHT)
            ThemeIntent.SetDarkIntent -> ThemeState(useDarkTheme = DarkThemeState.DARK)
        }
    }
}

sealed interface ThemeIntent {
    object SetSystemIntent : ThemeIntent
    object SetLightIntent : ThemeIntent
    object SetDarkIntent : ThemeIntent
}

data class ThemeState(
    val useDarkTheme: DarkThemeState
)

enum class DarkThemeState {
    SYSTEM, LIGHT, DARK
}

val ThemeStateDefault = ThemeState(useDarkTheme = DarkThemeState.SYSTEM)
