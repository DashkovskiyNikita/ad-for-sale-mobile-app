package com.dashkovskiy.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.Ad
import com.dashkovskiy.api.FavoriteAd
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.repository.AdsRepository
import com.dashkovskiy.repository.FavoriteAdsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AdsState(
    val search: String = "",
    val userFavoriteAds: List<Int> = emptyList(),
    val searchResult: List<Ad> = emptyList(),
    val isSearchMode: Boolean = false,
    val isUserAuthorized: Boolean = false,
    val isLoading: Boolean = false
) {
    fun isAdFavorite(id: Int) = userFavoriteAds.any { it == id }
}

class AdsSource(
    private val adsRepository: AdsRepository
) : PagingSource<Int, Ad>() {

    override fun getRefreshKey(state: PagingState<Int, Ad>) = state.anchorPosition


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Ad> {
        val page = params.key ?: 1
        return when (val adsResponse = adsRepository.getAds(page = page)) {
            is NetworkResponse.Success -> {
                LoadResult.Page(
                    data = adsResponse.body,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (adsResponse.body.isEmpty()) null else page + 1
                )
            }
            is NetworkResponse.NetworkError -> LoadResult.Error(adsResponse.error)
            is NetworkResponse.UnknownError -> LoadResult.Error(adsResponse.error)
            is NetworkResponse.ApiError -> LoadResult.Invalid()
        }
    }

}

class AdsViewModel(
    private val adsRepository: AdsRepository,
    private val favoriteAdsRepository: FavoriteAdsRepository,
    private val preferenceManager: PreferenceManager,
    adsPager: Pager<Int, Ad>
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdsState())
    val uiState = _uiState.asStateFlow()

    private var state: AdsState
        get() = _uiState.value
        set(newState) {
            _uiState.value = newState
        }

    val adsFlow = adsPager.flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            preferenceManager.isUserAuthorizedFlow.collectLatest { isAuthorized ->
                state = state.copy(isUserAuthorized = isAuthorized)
            }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            when (val result = favoriteAdsRepository.getUserFavoriteAds()) {
                is NetworkResponse.Success -> {
                    state = state.copy(userFavoriteAds = result.body.map { it.ad.id })
                }
                else -> {}
            }
        }
    }

    fun onFavoriteIntent(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                state = state.copy(
                    userFavoriteAds = state.userFavoriteAds.toMutableList().also { it.add(id) }
                )
                favoriteAdsRepository.newUserFavorite(id)
            } else {
                state = state.copy(
                    userFavoriteAds = state.userFavoriteAds.filter { it != id }
                )
                favoriteAdsRepository.deleteFavoriteByAdId(id)
            }

        }
    }

    fun clearSearch() {
        state = state.copy(search = "")
    }

    fun setSearch(search: String) {
        state = if (search.isEmpty() || search.isBlank())
            state.copy(search = "", isSearchMode = false)
        else
            state.copy(search = search)
    }

    fun trySearch() {
        viewModelScope.launch {
            state = state.copy(isSearchMode = true, isLoading = true)
            state = when (val searchResult = adsRepository.searchAds(query = state.search)) {
                is NetworkResponse.Success -> {
                    state.copy(searchResult = searchResult.body, isLoading = false)
                }
                else -> state.copy(isLoading = false)
            }
        }
    }
}