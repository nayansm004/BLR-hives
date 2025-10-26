package com.example.blrhives.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.blrhives.data.Hive
import com.example.blrhives.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController, currentUser: User) {
    var searchQuery by remember { mutableStateOf("") }

    val sampleHives = remember {
        listOf(
            Hive(
                id = "1",
                name = "Bangalore Run Club",
                description = "For runners of all levels to connect and share running routes",
                category = "Fitness",
                tags = listOf("running", "fitness", "marathon", "health"),
                memberCount = 324,
                coverImageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400"
            ),
            Hive(
                id = "2",
                name = "Bangalore Home Chefs",
                description = "Share recipes, cooking tips, and food experiences",
                category = "Food",
                tags = listOf("cooking", "recipes", "food", "baking"),
                memberCount = 156,
                coverImageUrl = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400"
            ),
            Hive(
                id = "3",
                name = "BLR Board Gamers",
                description = "Board game enthusiasts organizing meetups and game nights",
                category = "Gaming",
                tags = listOf("boardgames", "tabletop", "meetup", "strategy"),
                memberCount = 89,
                coverImageUrl = "https://images.unsplash.com/photo-1606092195730-5d7b9af1efc5?w=400"
            ),
            Hive(
                id = "4",
                name = "Bangalore Photographers",
                description = "Photography enthusiasts sharing techniques and organizing photo walks",
                category = "Art & Creative",
                tags = listOf("photography", "art", "creative", "photowalk"),
                memberCount = 267,
                coverImageUrl = "https://images.unsplash.com/photo-1452587925148-ce544e77e70d?w=400"
            )
        )
    }

    val filteredHives = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            sampleHives
        } else {
            sampleHives.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true) ||
                        it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Explore Hives", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search hives...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Create Hive Button
            Button(
                onClick = { navController.navigate("create_hive") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create")
                Text(" Create New Hive")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Discover Communities",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredHives) { hive ->
                HiveCard(
                    hive = hive,
                    onClick = { navController.navigate("hive_detail/${hive.id}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveCard(hive: Hive, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Cover Image
            AsyncImage(
                model = hive.coverImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = hive.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = hive.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = "Members",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            " ${hive.memberCount} members",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    AssistChip(
                        onClick = onClick,
                        label = { Text("Join") }
                    )
                }

                // Tags
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    hive.tags.take(3).forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text("#$tag", fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }
    }
}