package com.example.blrhives.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.blrhives.data.Conversation  // ADD THIS
import com.example.blrhives.data.ChatMessage  // ADD THIS (even though you define HiveChat locally)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(currentUser: User) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Hive Chats", "Direct Messages")

    val sampleHiveChats = remember {
        listOf(
            HiveChat(
                hiveId = "1",
                hiveName = "Bangalore Run Club",
                lastMessage = "Anyone joining for tomorrow's morning run?",
                lastMessageTime = java.time.LocalDateTime.now().minusMinutes(15),
                unreadCount = 3
            ),
            HiveChat(
                hiveId = "2",
                hiveName = "Bangalore Home Chefs",
                lastMessage = "Just shared my lasagna recipe in the group!",
                lastMessageTime = java.time.LocalDateTime.now().minusHours(2),
                unreadCount = 0
            )
        )
    }

    val sampleDirectChats = remember {
        listOf(
            Conversation(
                id = "1",
                otherUserName = "Rohan Kumar",
                lastMessage = "Thanks for the running tips!",
                lastMessageTime = java.time.LocalDateTime.now().minusMinutes(30),
                otherUserProfilePic = "https://ui-avatars.com/api/?name=Rohan Kumar&background=random"
            ),
            Conversation(
                id = "2",
                otherUserName = "Priya Sharma",
                lastMessage = "The sourdough recipe worked perfectly!",
                lastMessageTime = java.time.LocalDateTime.now().minusHours(4),
                otherUserProfilePic = "https://ui-avatars.com/api/?name=Priya Sharma&background=random"
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Chats", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

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
            0 -> HiveChatsTab(sampleHiveChats)
            1 -> DirectMessagesTab(sampleDirectChats)
        }
    }
}

data class HiveChat(
    val hiveId: String,
    val hiveName: String,
    val lastMessage: String,
    val lastMessageTime: java.time.LocalDateTime,
    val unreadCount: Int
)

@Composable
fun HiveChatsTab(hiveChats: List<HiveChat>) {
    if (hiveChats.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Chat,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "No hive chats yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                "Join some hives to start chatting!",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn {
            items(hiveChats) { chat ->
                HiveChatItem(chat)
            }
        }
    }
}

@Composable
fun DirectMessagesTab(conversations: List<Conversation>) {
    if (conversations.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Message,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "No messages yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                "Start a conversation with someone!",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn {
            items(conversations) { conversation ->
                ConversationItem(conversation)
            }
        }
    }
}

@Composable
fun HiveChatItem(chat: HiveChat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to hive chat */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = "https://ui-avatars.com/api/?name=${chat.hiveName}&background=random",
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            if (chat.unreadCount > 0) {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        chat.unreadCount.toString(),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = chat.hiveName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        Text(
            text = chat.lastMessageTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ConversationItem(conversation: Conversation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to private chat */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = conversation.otherUserProfilePic,
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
                text = conversation.otherUserName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = conversation.lastMessage,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        Text(
            text = conversation.lastMessageTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}