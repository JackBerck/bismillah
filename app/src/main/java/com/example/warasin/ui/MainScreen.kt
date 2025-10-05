package com.example.warasin.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warasin.ui.healthnotes.HealthNotesScreen
import com.example.warasin.ui.homepage.HomepageScreen
import com.example.warasin.ui.medicine.MedicineScreen
import com.example.warasin.ui.navigation.MyBottomNavigationBar
import com.example.warasin.ui.profile.ProfileScreen

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_screen") {
                HomepageScreen(
                    navController = navController,
                    viewModel = hiltViewModel()
                )
            }

            composable("medicine_screen") {
                MedicineScreen()
            }

            composable("note_screen") {
                HealthNotesScreen()
            }

            composable("profile_screen") {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}
