package com.dashkovskiy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.api.UserInfo
import com.dashkovskiy.repository.AuthorizationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class UserProfileState(
    val isUserAuthorized: Boolean = false,
    val userInfo: UserInfo? = null
) {
    val fullName: String
        get() = "${userInfo?.name} ${userInfo?.surname}"
}

class UserProfileViewModel(
    private val authorizationRepository: AuthorizationRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState = _uiState.asStateFlow()

    private var state: UserProfileState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    init {
        viewModelScope.launch {
            preferenceManager.isUserAuthorizedFlow.collectLatest { isAuthorized ->
                state = state.copy(isUserAuthorized = isAuthorized)
                if (isAuthorized) loadUserInfo()
            }
        }
    }

    private suspend fun loadUserInfo() {
        when (val info = authorizationRepository.userInfo()) {
            is NetworkResponse.Success -> {
                state = state.copy(userInfo = info.body)
            }
            else -> {}
        }
    }

}