package org.hacorp.newsfeed.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.hacorp.newsfeed.model.NavigationComponent


data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun BottomNavigationBar(
    currentChild: NavigationComponent.Child,
    navigationComponent: NavigationComponent
) {
    val items = listOf(
        BottomNavItem(
            label = "News",
            selectedIcon = Icons.AutoMirrored.Filled.Article,
            unselectedIcon = Icons.AutoMirrored.Outlined.Article,
            onClick = { navigationComponent.navigateToNewsList() }
        ),
        BottomNavItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            onClick = { navigationComponent.navigateToProfile() }
        ),
        BottomNavItem(
            label = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            onClick = { navigationComponent.navigateToSettings() }
        )
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = when (currentChild) {
                is NavigationComponent.Child.NewsList -> item.label == "News"
//                is NavigationComponent.Child.NewsDetail -> item.label == "News"
                is NavigationComponent.Child.Profile -> item.label == "Profile"
                is NavigationComponent.Child.Settings -> item.label == "Settings"
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = item.onClick,
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}
