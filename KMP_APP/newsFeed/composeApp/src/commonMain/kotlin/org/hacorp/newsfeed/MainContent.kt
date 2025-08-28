package org.hacorp.newsfeed


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.hacorp.newsfeed.components.BottomNavigationBar
import org.hacorp.newsfeed.model.AuthState
import org.hacorp.newsfeed.model.AuthStore
import org.hacorp.newsfeed.model.NavigationComponent
import org.hacorp.newsfeed.model.NewsStore
import org.hacorp.newsfeed.model.ThemeState
import org.hacorp.newsfeed.model.ThemeStore
import org.hacorp.newsfeed.pages.LoginPage
import org.hacorp.newsfeed.pages.NewsDetailPage
import org.hacorp.newsfeed.pages.NewsListPage
import org.hacorp.newsfeed.pages.ProfilePage
import org.hacorp.newsfeed.pages.SettingsPage
import org.hacorp.newsfeed.ui.theme.NewsFeedTheme

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    navigationComponent: NavigationComponent,
    authStore: AuthStore,
    newsStore: NewsStore,
    themeStore: ThemeStore
) {
    val stack by navigationComponent.stack.subscribeAsState()
    val authState: AuthState by authStore.state.collectAsState()
    val themeState: ThemeState by themeStore.state.collectAsState()

    // Handle authentication state changes
    LaunchedEffect(authState) {
        try {
            when (authState) {
                is AuthState.Authenticated -> {
                    val currentChild = stack.active.instance
                    if (currentChild is NavigationComponent.Child.Login || currentChild is NavigationComponent.Child.Splash) {
                        navigationComponent.navigateToNewsList()
                    }
                }
                is AuthState.NotAuthenticated -> {
                    val currentChild = stack.active.instance
                    if (currentChild !is NavigationComponent.Child.Login ) {
                        navigationComponent.navigateToLogin()
                    }
                }
                is AuthState.Loading -> {
                    // Keep current state during loading
                }
                else -> {
                    navigationComponent.navigateToNewsList()
                }
            }
        } catch (e: Exception) {
            // Handle navigation errors gracefully
            println("Navigation error: ${e.message}")
        }
    }

    NewsFeedTheme(themeState = themeState) {
        when (val child = stack.active.instance) {
            is NavigationComponent.Child.Login -> {
                LoginPage(
                    authStore = authStore,
                    modifier = modifier
                )
            }
            else -> {
                Scaffold(
                    modifier = modifier,
                    bottomBar = {
                        if (authState is AuthState.Authenticated && 
                            child !is NavigationComponent.Child.Login && 
                            child !is NavigationComponent.Child.Splash) {
                            BottomNavigationBar(
                                currentChild = child,
                                navigationComponent = navigationComponent
                            )
                        }
                    }
                ) { paddingValues ->
                    Column {
                        Spacer(
                            Modifier
                                .padding(top = topPadding)
                                .align(Alignment.CenterHorizontally)
                        )
                        
                        when (child) {
                            is NavigationComponent.Child.NewsList -> {
                                NewsListPage(
                                    newsStore = newsStore,
                                    navigationStore = navigationComponent,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                            is NavigationComponent.Child.NewsDetail -> {
                                NewsDetailPage(
                                    articleId = child.articleId,
                                    newsStore = newsStore,
                                    navigationStore = navigationComponent,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                            is NavigationComponent.Child.Profile -> {
                                ProfilePage(
                                    authStore = authStore,
                                    navigationStore = navigationComponent,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                            is NavigationComponent.Child.Settings -> {
                                SettingsPage(
                                    themeStore = themeStore,
                                    navigationStore = navigationComponent,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                            else -> {
                                if (authState is AuthState.Authenticated) {
                                    NewsListPage(
                                        newsStore = newsStore,
                                        navigationStore = navigationComponent,
                                        modifier = Modifier.padding(paddingValues)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
