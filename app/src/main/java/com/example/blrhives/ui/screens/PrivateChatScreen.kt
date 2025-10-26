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
import com.example.blrhives.data.ChatMessage  // ADD THIS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateChatScreen(
    otherUserId: String,
    currentUser: User,
    navController: NavController
) {
    var messageText by remember { mutableStateOf("") }

    val otherUser = remember {
        User(
            id = otherUserId,
            displayName = "Rohan Kumar",
            bio = "Marathon runner and fitness enthusiast",
            profileImageUrl = "https://ui-avatars.com/api/?name=Rohan Kumar&background=random"
        )
    }

    val sampleMessages = remember {
        listOf(
            ChatMessage(
                id = "1",
                text = "Hey! I saw your post about the running tips. Really helpful stuff!",
                senderId = currentUser.id,
                senderName = currentUser.displayName,
                timestamp = java.time.LocalDateTime.now().minusHours(2),
                isPrivate = true
            ),
            ChatMessage(
                id = "2",
                text = "Thanks! I'm glad you found it useful. Are you planning to join tomorrow's group run?",
                senderId = otherUser.id,
                senderName = otherUser.displayName,
                timestamp = java.time.LocalDateTime.now().minusHours(1),
                isPrivate = true
            ),
            ChatMessage(
                id = "3",
                text = "Definitely! What time should I be there?",
                senderId = currentUser.id,
                senderName = currentUser.displayName,
                timestamp = java.time.LocalDateTime.now().minusMinutes(30),
                isPrivate = true
            ),
            ChatMessage(
                id = "4",
                text = "6 AM sharp at Cubbon Park main gate. See you there! 🏃‍♂️",
                senderId = otherUser.id,
                senderName = otherUser.displayName,
                timestamp = java.time.LocalDateTime.now().minusMinutes(15),
                isPrivate = true
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = otherUser.profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = otherUser.displayName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Video call */ }) {
                    Icon(Icons.Default.VideoCall, contentDescription = "Video Call")
                }
                IconButton(onClick = { /* Voice call */ }) {
                    Icon(Icons.Default.Call, contentDescription = "Voice Call")
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleMessages) { message ->
                PrivateMessageItem(message, currentUser)
            }
        }

        // Message input
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(onClick = { /* Attach file */ }) {
                    Icon(Icons.Default.AttachFile, contentDescription = "Attach")
                }

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f),
                    maxLines = 4
                )

                IconButton(onClick = { /* Add emoji */ }) {
                    Icon(Icons.Default.EmojiEmotions, contentDescription = "Emoji")
                }

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            // Send message logic
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (messageText.isNotBlank()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PrivateMessageItem(message: ChatMessage, currentUser: User) {
    val isOwnMessage = message.senderId == currentUser.id

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOwnMessage) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                    bottomEnd = if (isOwnMessage) 4.dp else 16.dp
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
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = if (isOwnMessage) 0.dp else 8.dp,
                    end = if (isOwnMessage) 8.dp else 0.dp
                )
            )
        }
    }
}