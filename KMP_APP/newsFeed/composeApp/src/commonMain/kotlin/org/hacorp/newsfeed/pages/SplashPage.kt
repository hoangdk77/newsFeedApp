package org.hacorp.newsfeed.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Article
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashPage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Article,
                contentDescription = "News Feed Logo",
                modifier = Modifier.size(80.dp),
                tint = Color.White
            )
            Text(
                text = "NewsFeed",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Stay Informed",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
