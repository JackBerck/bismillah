package com.example.warasin.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.warasin.ui.theme.Gray50

@Composable
fun MyBottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomBarRoutes = listOf("login", "register")
    if (currentRoute in hideBottomBarRoutes) return

    NavigationBar(
        modifier = Modifier
            .background(Gray50)
            .drawBehind {
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        containerColor = Gray50
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Biar gak numpuk route di back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (currentRoute == item.route)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray
                    )
                }
            )
        }
    }
}
