package com.example.blrhives.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.blrhives.data.Post
import com.example.blrhives.data.User
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(currentUser: User) {
    val samplePosts = remember {
        listOf(
            Post(
                id = "1",
                text = "Just completed my morning 10K run around Cubbon Park! The weather was perfect. Anyone else running today?",
                authorName = "Rohan Kumar",
                hiveName = "Bangalore Run Club",
                timestamp = java.time.LocalDateTime.now().minusHours(2),
                likes = 15,
                comments = 3
            ),
            Post(
                id = "2",
                text = "Made sourdough bread for the first time! Here's my attempt. Any tips for getting a better crust?",
                imageUrl = "https://images.unsplash.com/photo-1549931319-a545dcf3bc73?w=400",
                authorName = "Priya Sharma",
                hiveName = "Bangalore Home Chefs",
                timestamp = java.time.LocalDateTime.now().minusHours(4),
                likes = 28,
                comments = 8
            ),
            Post(
                id = "3",
                text = "Board game night this Saturday at my place in Koramangala. We have Catan, Ticket to Ride, and more. Who's in?",
                authorName = "Arjun Nair",
                hiveName = "BLR Board Gamers",
                timestamp = java.time.LocalDateTime.now().minusHours(6),
                likes = 12,
                comments = 15
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "Home Feed",
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        if (samplePosts.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.ExploreOff,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    "Your feed is empty!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    "Join some Hives to see posts here",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Button(
                    onClick = { /* Navigate to explore */ },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Explore Hives")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(samplePosts) { post ->
                    PostCard(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
