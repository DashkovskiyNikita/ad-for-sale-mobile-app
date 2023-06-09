package com.dashkovskiy.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.Ad
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.repository.AdsRepository
import com.dashkovskiy.repository.FavoriteAdsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class AdDetailsState(
    val ad: Ad? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isUserAuthorized: Boolean = false
)

class AdDetailsViewModel(
    private val adsRepository: AdsRepository,
    private val favoriteAdsRepository: FavoriteAdsRepository,
    private val preferenceManager: PreferenceManager,
    adId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdDetailsState())
    val uiState = _uiState.asStateFlow()

    private var state: AdDetailsState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    init {
        viewModelScope.launch {
            launch {
                getAdById(id = adId)
            }
            launch {
                preferenceManager.isUserAuthorizedFlow.collectLatest { isAuthorized ->
                    state = state.copy(isUserAuthorized = isAuthorized)
                    if (isAuthorized) getUserFavoriteAds()
                }
            }
        }
    }

    private suspend fun getAdById(id: Int) {
        state = state.copy(isLoading = true)
        state = when (val ad = adsRepository.getAdById(id)) {
            is NetworkResponse.Success -> {
                state.copy(ad = ad.body, isLoading = false)
            }
            else -> {
                state.copy(isLoading = false)
            }
        }
    }

    private suspend fun getUserFavoriteAds() {
        when (val favoriteAds = favoriteAdsRepository.getUserFavoriteAds()) {
            is NetworkResponse.Success -> {
                state = state.copy(
                    isFavorite = favoriteAds.body.any { it.id == state.ad?.id }
                )
            }
            else -> {}
        }
    }

    fun setFavorite() {
        state = state.copy(isFavorite = !state.isFavorite)
        viewModelScope.launch {
            val id = state.ad?.id ?: return@launch
            if (state.isFavorite) {
                favoriteAdsRepository.newUserFavorite(id)
            } else {
                favoriteAdsRepository.deleteFavoriteByAdId(id)
            }
        }
    }
}