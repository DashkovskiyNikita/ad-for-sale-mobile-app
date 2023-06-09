package com.dashkovskiy.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.api.FavoriteAd
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.repository.FavoriteAdsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavoriteAdsState(
    val favoriteAds: List<FavoriteAd> = emptyList(),
    val isLoading: Boolean = false
)

class FavoriteAdsViewModel(
    private val favoriteAdsRepository: FavoriteAdsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteAdsState())
    val uiState = _uiState.asStateFlow()

    private var state: FavoriteAdsState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    fun getUserFavoriteAds(){
        viewModelScope.launch {
            when (val result = favoriteAdsRepository.getUserFavoriteAds()) {
                is NetworkResponse.Success -> state = state.copy(favoriteAds = result.body)
                else -> {}
            }
        }
    }

    fun deleteFavorite(id: Int) {
        viewModelScope.launch {
            when (favoriteAdsRepository.deleteUserFavorite(id = id)) {
                is NetworkResponse.Success -> {
                    val favoriteAds = state.favoriteAds.filter { it.id != id }
                    state = state.copy(favoriteAds = favoriteAds)
                }
                else -> {}
            }
        }
    }
}