package com.example.blrhives


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.blrhives.data.User  // ADD THIS
import com.example.blrhives.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLRHivesApp() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<User?>(null) }

    if (currentUser == null) {
        AuthScreen(onAuthSuccess = { user -> currentUser = user })
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    val items = listOf(
                        BottomNavItem("home", "Home", Icons.Default.Home),
                        BottomNavItem("explore", "Explore", Icons.Default.Explore),
                        BottomNavItem("chat", "Chat", Icons.Default.Chat),
                        BottomNavItem("profile", "Profile", Icons.Default.Person)
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") { HomeScreen(currentUser!!) }
                composable("explore") { ExploreScreen(navController, currentUser!!) }
                composable("chat") { ChatScreen(currentUser!!) }
                composable("profile") { ProfileScreen(currentUser!!) }
                composable("create_hive") { CreateHiveScreen(navController, currentUser!!) }
                composable("hive_detail/{hiveId}") { backStackEntry ->
                    val hiveId = backStackEntry.arguments?.getString("hiveId") ?: ""
                    HiveDetailScreen(hiveId, navController, currentUser!!)
                }
                composable("private_chat/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    PrivateChatScreen(userId, currentUser!!, navController)
                }
            }
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)