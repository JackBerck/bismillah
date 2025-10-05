package com.example.warasin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warasin.ui.theme.WarasInTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import com.example.warasin.ui.auth.AuthViewModel
import com.example.warasin.ui.navigation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WarasInTheme {
                val navController = rememberNavController()
                val authViewModel = hiltViewModel<AuthViewModel>()
                AppNavigation(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}