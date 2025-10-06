package com.example.warasin.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.warasin.ui.MainScreen
import com.example.warasin.ui.auth.AuthViewModel
import com.example.warasin.ui.auth.LoginScreen
import com.example.warasin.ui.auth.RegistrationScreen
import com.example.warasin.ui.onboarding.OnboardingScreen
import com.example.warasin.ui.onboarding.OnboardingViewModel

object Graph {
    const val ROOT = "root_graph"
    const val ONBOARDING = "onboarding_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigation(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        composable(route = Graph.ONBOARDING) {
            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onFinishClick = {
                    onboardingViewModel.saveOnBoardingState(completed = true)
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        authNavGraph(navController = navController)

        composable(route = Graph.MAIN) {
            val authViewModel: AuthViewModel = hiltViewModel()
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
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
    }
}

