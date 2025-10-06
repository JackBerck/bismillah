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
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding Graph
        navigation(
            startDestination = "onboarding_screen",
            route = Graph.ONBOARDING
        ) {
            composable("onboarding_screen") {
                OnboardingScreen(
                    onFinishClick = {
                        navController.navigate(Graph.AUTHENTICATION) {
                            popUpTo(Graph.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Authentication Graph
        navigation(
            startDestination = "login_screen",
            route = Graph.AUTHENTICATION
        ) {
            composable("login_screen") {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate("register_screen")
                    },
                    onLoginSuccess = {
                        navController.navigate(Graph.ROOT) {
                            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        }
                    }
                )
            }

            composable("register_screen") {
                RegistrationScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(Graph.ROOT) {
                            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Root Graph (Main App)
        composable(Graph.ROOT) {
            MainScreen(
                onLogout = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) { inclusive = true }
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

