package com.example.warasin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.warasin.ui.navigation.AppNavigation
import com.example.warasin.ui.theme.WarasInTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.uiState.value == MainActivityUiState.Loading
        }
        enableEdgeToEdge()
        setContent {
            WarasInTheme {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

                when (uiState) {
                    is MainActivityUiState.Loading -> {
                    }
                    is MainActivityUiState.Success -> {
                        AppNavigation(
                            navController = rememberNavController(),
                            startDestination = uiState.startDestination
                        )
                    }
                }
            }
        }
    }
}
