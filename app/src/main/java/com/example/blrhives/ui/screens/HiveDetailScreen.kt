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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.time.format.DateTimeFormatter
import com.example.blrhives.data.User  // ADD THIS
import com.example.blrhives.data.Hive  // ADD THIS
import com.example.blrhives.data.Post  // ADD THIS
import com.example.blrhives.data.ChatMessage  // ADD THIS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveDetailScreen(hiveId: String, navController: NavController, currentUser: User) {
    var selectedTab by remember { mutableStateOf(0) }
    var isJoined by remember { mutableStateOf(false) }
    var newPostText by remember { mutableStateOf("") }
    val tabs = listOf("Posts", "Chat", "Members")

    val sampleHive = remember {
        Hive(
            id = hiveId,
            name = "Bangalore Run Club",
            description = "For runners of all levels to connect and share running routes. We organize weekly group runs and share tips for better performance.",
            category = "Fitness",
            tags = listOf("running", "fitness", "marathon", "health"),
            memberCount = 324,
            coverImageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400"
        )
    }

    val samplePosts = remember {
        listOf(
            Post(
                id = "1",
                text = "Great turnout for today's morning run! Next week we're planning a 15K route through Lalbagh. Who's in?",
                authorName = "Rohan Kumar",
                hiveName = sampleHive.name,
                timestamp = java.time.LocalDateTime.now().minusHours(2),
                likes = 15,
                comments = 8
            ),
            Post(
                id = "2",
                text = "Tips for running in Bangalore weather: Start early, carry water, and don't forget sunscreen!",
                authorName = "Priya Nair",
                hiveName = sampleHive.name,
                timestamp = java.time.LocalDateTime.now().minusHours(6),
                likes = 32,
                comments = 12
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(sampleHive.name, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Share hive */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        // Hive Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AsyncImage(
                    model = sampleHive.coverImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = sampleHive.description,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Group,
                                contentDescription = "Members",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                " ${sampleHive.memberCount} members",
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { isJoined = !isJoined },
                            colors = if (isJoined) {
                                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            } else {
                                ButtonDefaults.buttonColors()
                            }
                        ) {
                            Text(if (isJoined) "Joined" else "Join")
                        }
                    }
                }
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Posts Tab
                Column(modifier = Modifier.fillMaxSize()) {
                    if (isJoined) {
                        // Post creation area
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                OutlinedTextField(
                                    value = newPostText,
                                    onValueChange = { newPostText = it },
                                    placeholder = { Text("Share something with the community...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { /* Add image */ }) {
                                        Icon(Icons.Default.Image, contentDescription = "Add Image")
                                        Text(" Photo")
                                    }

                                    Button(
                                        onClick = {
                                            // Post logic
                                            newPostText = ""
                                        },
                                        enabled = newPostText.isNotBlank()
                                    ) {
                                        Text("Post")
                                    }
                                }
                            }
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(samplePosts) { post ->
                            PostCard(post)
                        }
                    }
                }
            }
            1 -> {
                // Chat Tab
                HiveChatView(sampleHive, currentUser, isJoined)
            }
            2 -> {
                // Members Tab
                HiveMembersView(sampleHive)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveChatView(hive: Hive, currentUser: User, isJoined: Boolean) {
    var messageText by remember { mutableStateOf("") }

    val sampleMessages = remember {
        listOf(
            ChatMessage(
                id = "1",
                text = "Anyone joining for tomorrow's morning run at 6 AM?",
                senderName = "Rohan Kumar",
                timestamp = java.time.LocalDateTime.now().minusMinutes(10)
            ),
            ChatMessage(
                id = "2",
                text = "I'm in! Where's the meeting point?",
                senderName = "Priya Nair",
                timestamp = java.time.LocalDateTime.now().minusMinutes(5)
            ),
            ChatMessage(
                id = "3",
                text = "Cubbon Park main gate as usual",
                senderName = "Arjun Shah",
                timestamp = java.time.LocalDateTime.now().minusMinutes(2)
            )
        )
    }

    if (!isJoined) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Join to participate in chat",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleMessages) { message ->
                    ChatMessageItem(message, currentUser)
                }
            }

            // Message input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 4
                    )

                    IconButton(
                        onClick = {
                            // Send message logic
                            messageText = ""
                        },
                        enabled = messageText.isNotBlank()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, currentUser: User) {
    val isOwnMessage = message.senderName == currentUser.displayName

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwnMessage) {
            AsyncImage(
                model = "https://ui-avatars.com/api/?name=${message.senderName}&background=random",
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (!isOwnMessage) {
                Text(
                    text = message.senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOwnMessage) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    color = if (isOwnMessage) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Text(
                text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (isOwnMessage) {
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = "https://ui-avatars.com/api/?name=${currentUser.displayName}&background=random",
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun HiveMembersView(hive: Hive) {
    val sampleMembers = remember {
        listOf(
            User(id = "1", displayName = "Rohan Kumar", bio = "Marathon runner, 10+ years experience"),
            User(id = "2", displayName = "Priya Nair", bio = "Weekend warrior, love trail running"),
            User(id = "3", displayName = "Arjun Shah", bio = "Running coach and fitness enthusiast"),
            User(id = "4", displayName = "Sneha Gupta", bio = "New to running, excited to learn!")
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sampleMembers) { member ->
            MemberItem(member)
        }
    }
}

@Composable
fun MemberItem(member: User) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://ui-avatars.com/api/?name=${member.displayName}&background=random",
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = member.displayName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                if (member.bio.isNotEmpty()) {
                    Text(
                        text = member.bio,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }

            OutlinedButton(onClick = { /* Follow/Message */ }) {
                Text("Follow")
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://ui-avatars.com/api/?name=${post.authorName}&background=random")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(
                        text = post.authorName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "in ${post.hiveName} • ${post.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }

            // Content
            if (post.text.isNotEmpty()) {
                Text(
                    text = post.text,
                    modifier = Modifier.padding(top = 12.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // Image if available
            if (post.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    TextButton(onClick = { /* Like */ }) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Like",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            " ${post.likes}",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    TextButton(onClick = { /* Comment */ }) {
                        Icon(
                            Icons.Default.Comment,
                            contentDescription = "Comment",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            " ${post.comments}",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                TextButton(onClick = { /* Share */ }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(" Share")
                }
            }
        }
    }
}