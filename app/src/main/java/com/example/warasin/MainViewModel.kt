package com.example.warasin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warasin.data.DataStoreRepository
import com.example.warasin.data.repository.AuthRepository
import com.example.warasin.ui.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            repository.readOnBoardingState().collect { onboardingCompleted ->
                val isAuthenticated = authRepository.checkAuthState()

                val startDestination = when {
                    !onboardingCompleted -> Graph.ONBOARDING
                    !isAuthenticated -> Graph.AUTHENTICATION
                    else -> Graph.ROOT
                }

                _uiState.value = MainActivityUiState.Success(startDestination)
            }
        }
    }
}