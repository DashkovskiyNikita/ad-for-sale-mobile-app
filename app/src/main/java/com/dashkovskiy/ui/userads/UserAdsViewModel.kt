package com.dashkovskiy.ui.userads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.api.UserAd
import com.dashkovskiy.repository.AdsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserAdsState(
    val userAds: List<UserAd> = emptyList(),
    val isLoading: Boolean = false
)

class UserAdsViewModel(
    private val adsRepository: AdsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAdsState())
    val uiState = _uiState.asStateFlow()

    private var state: UserAdsState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    init {
        viewModelScope.launch {
            when (val result = adsRepository.getUserAds()) {
                is NetworkResponse.Success -> state = state.copy(userAds = result.body)
                else -> {}
            }
        }
    }

    fun deleteUserAd(id: Int) {
        viewModelScope.launch {
            when (adsRepository.deleteUserAd(id = id)) {
                is NetworkResponse.Success -> {
                    val userAds = state.userAds.filter { it.id != id }
                    state = state.copy(userAds = userAds)
                }
                else -> {}
            }
        }
    }
}