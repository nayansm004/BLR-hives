package com.example.blrhives.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blrhives.data.User
import com.example.blrhives.data.Hive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHiveScreen(navController: NavController, currentUser: User) {
    var hiveName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Fitness", "Food", "Gaming", "Art & Creative", "Tech", "Music",
        "Books", "Travel", "Business", "Education", "Photography", "Sports"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Create New Hive", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Basic Information Section
        Text(
            "Basic Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = hiveName,
            onValueChange = { if (it.length <= 50) hiveName = it },
            label = { Text("Hive Name") },
            placeholder = { Text("e.g., Bangalore Run Club") },
            supportingText = { Text("${hiveName.length}/50 characters") },
            leadingIcon = {
                Icon(Icons.Default.Group, contentDescription = "Hive Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { if (it.length <= 200) description = it },
            label = { Text("Description") },
            placeholder = { Text("Brief description of your community") },
            supportingText = { Text("${description.length}/200 characters") },
            leadingIcon = {
                Icon(Icons.Default.Description, contentDescription = "Description")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                placeholder = { Text("Select a category") },
                leadingIcon = {
                    Icon(Icons.Default.Category, contentDescription = "Category")
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        },
                        text = { Text(category) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Privacy Settings Section
        Text(
            "Privacy Settings",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (isPrivate) Icons.Default.Lock else Icons.Default.Public,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                    Text(
                        if (isPrivate) "Private Community" else "Public Community",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (isPrivate) "Members need approval to join" else "Anyone can join instantly",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = isPrivate,
                    onCheckedChange = { isPrivate = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Community Guidelines
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "As the creator, you'll be the admin",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "You'll be able to moderate posts, manage members, and customize community settings.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Create Button
        Button(
            onClick = {
                // Create hive logic
                val newHive = Hive(
                    id = "hive_${System.currentTimeMillis()}",
                    name = hiveName,
                    description = description,
                    category = selectedCategory,
                    isPrivate = isPrivate,
                    creatorId = currentUser.id,
                    memberCount = 1 // Creator is first member
                )

                // In real app, save to Firebase here
                // FirebaseFirestore.getInstance().collection("hives").add(newHive)

                navController.popBackStack()
            },
            enabled = hiveName.isNotBlank() && description.isNotBlank() && selectedCategory.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Create, contentDescription = "Create")
            Text(" Create Hive", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}