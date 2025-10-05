package com.example.warasin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.warasin.ui.healthnotes.HealthNotesScreen
import com.example.warasin.ui.homepage.HomepageScreen
import com.example.warasin.ui.medicine.MedicineScreen
import com.example.warasin.ui.profile.ProfileScreen
import com.example.warasin.ui.schedule.ScheduleScreen
import com.example.warasin.ui.auth.LoginScreen
import com.example.warasin.ui.auth.RegistrationScreen
import com.example.warasin.ui.MainScreen
import com.example.warasin.ui.auth.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Check if user is already logged in
    val isLoggedIn = authViewModel.checkAuthState()
    val startDestination = if (isLoggedIn) "main" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegistrationScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}