package com.example.warasin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.warasin.ui.healthnotes.HealthNotesScreen
import com.example.warasin.ui.homepage.HomepageScreen
import com.example.warasin.ui.medicine.MedicineScreen
import com.example.warasin.ui.profile.ProfileScreen


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home_screen",
        modifier = modifier
    ) {
        composable("home_screen") { HomepageScreen(
            navController
        ) }
        composable("medicine_screen") { MedicineScreen() }
        composable("history_screen") { /* Composable untuk layar Riwayat */ }
        composable("note_screen") { HealthNotesScreen() }
        composable("profile_screen") { ProfileScreen() }
    }
}