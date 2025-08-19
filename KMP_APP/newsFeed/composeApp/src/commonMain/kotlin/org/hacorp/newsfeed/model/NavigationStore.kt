package org.hacorp.newsfeed.model

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class NavigationComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    private val navigation = StackNavigation<Config>()
    
    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Splash,
        handleBackButton = true,
        childFactory = ::child
    )
    
    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Splash -> Child.Splash
            is Config.Login -> Child.Login
            is Config.NewsList -> Child.NewsList
            is Config.NewsDetail -> Child.NewsDetail(config.articleId)
            is Config.Profile -> Child.Profile
            is Config.Settings -> Child.Settings
        }
    
    @OptIn(DelicateDecomposeApi::class)
    fun navigateToLogin() = navigation.push(Config.Login)
    @OptIn(DelicateDecomposeApi::class)
    fun navigateToNewsList() = navigation.push(Config.NewsList)
    @OptIn(DelicateDecomposeApi::class)
    fun navigateToNewsDetail(articleId: String) = navigation.push(Config.NewsDetail(articleId))
    @OptIn(DelicateDecomposeApi::class)
    fun navigateToProfile() = navigation.push(Config.Profile)
    @OptIn(DelicateDecomposeApi::class)
    fun navigateToSettings() = navigation.push(Config.Settings)
    fun navigateBack() = navigation.pop()
    @OptIn(DelicateDecomposeApi::class)
    fun logout() = navigation.push(Config.Login)
    
    @Serializable
    sealed interface Config {
        @Serializable
        data object Splash : Config
        @Serializable
        data object Login : Config
        @Serializable
        data object NewsList : Config
        @Serializable
        data class NewsDetail(val articleId: String) : Config
        @Serializable
        data object Profile : Config
        @Serializable
        data object Settings : Config
    }
    
    sealed interface Child {
        data object Splash : Child
        data object Login : Child
        data object NewsList : Child
        data class NewsDetail(val articleId: String) : Child
        data object Profile : Child
        data object Settings : Child
    }
}
