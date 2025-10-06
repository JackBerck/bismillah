package com.example.warasin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.DataStoreRepository
import com.example.warasin.data.repository.AuthRepository
import com.example.warasin.ui.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainActivityUiState {
    data object Loading : MainActivityUiState()
    data class Success(val startDestination: String) : MainActivityUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Combine both flows to avoid race condition
            combine(
                repository.readOnBoardingState(),
                repository.readAuthState()
            ) { onboardingCompleted, isLoggedInDataStore ->

                Log.d("MainViewModel", "=== APP STARTUP CHECK ===")
                Log.d("MainViewModel", "Onboarding completed: $onboardingCompleted")
                Log.d("MainViewModel", "DataStore logged in: $isLoggedInDataStore")

                // Double check dengan Firebase dan SharedPreferences
                val isAuthenticated = authRepository.checkAuthState()
                Log.d("MainViewModel", "Final auth state: $isAuthenticated")

                val startDestination = when {
                    !onboardingCompleted -> {
                        Log.d("MainViewModel", "Navigate to: ONBOARDING")
                        Graph.ONBOARDING
                    }
                    !isAuthenticated -> {
                        Log.d("MainViewModel", "Navigate to: AUTHENTICATION")
                        Graph.AUTHENTICATION
                    }
                    else -> {
                        Log.d("MainViewModel", "Navigate to: ROOT")
                        Graph.ROOT
                    }
                }

                _uiState.value = MainActivityUiState.Success(startDestination)
            }.collect { /* Flow sudah di-handle di dalam combine */ }
        }
    }
}