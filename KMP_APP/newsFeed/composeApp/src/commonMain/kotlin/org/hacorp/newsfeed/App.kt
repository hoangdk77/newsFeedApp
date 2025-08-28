package org.hacorp.newsfeed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.hacorp.newsfeed.animations.newSplashTransition
import org.hacorp.newsfeed.model.AuthStore
import org.hacorp.newsfeed.model.DarkThemeState
import org.hacorp.newsfeed.model.NavigationComponent
import org.hacorp.newsfeed.model.NewsStore
import org.hacorp.newsfeed.model.ThemeState
import org.hacorp.newsfeed.model.ThemeStore
import org.hacorp.newsfeed.pages.SplashPage
import org.hacorp.newsfeed.ui.theme.NewsFeedTheme
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@Composable
fun App() {
    val componentContext = remember { DefaultComponentContext(lifecycle = LifecycleRegistry() as Lifecycle) }
    val navigationComponent = remember { NavigationComponent(componentContext) }
    val authStore = remember { AuthStore() }
    val newsStore = remember { NewsStore() }
    val themeStore = remember { ThemeStore() }

    val transition = newSplashTransition()

    Box(modifier = Modifier.safeContentPadding()) {
        SplashPage(
            modifier = Modifier.alpha(transition.splashAlpha)
        )

        MainContent(
            modifier = Modifier.alpha(transition.contentAlpha),
            topPadding = transition.contentTopPadding,
            navigationComponent = navigationComponent,
            authStore = authStore,
            newsStore = newsStore,
            themeStore = themeStore
        )
    }
}
