package com.dashkovskiy.repository

import com.dashkovskiy.api.FavoriteAdsApi
import com.dashkovskiy.api.NetworkErrorWrapper
import com.dashkovskiy.api.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteAdsRepository(
    private val favoriteAdsApi: FavoriteAdsApi,
    private val networkErrorWrapper: NetworkErrorWrapper
) {
    suspend fun getUserFavoriteAds() = withContext(Dispatchers.IO) {
        runCatching {
            val userFavorites = favoriteAdsApi.userFavorites()
            NetworkResponse.Success(body = userFavorites)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun newUserFavorite(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            favoriteAdsApi.newFavorite(id = id)
            NetworkResponse.Success(body = true)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun deleteUserFavorite(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            favoriteAdsApi.deleteFavorite(id = id)
            NetworkResponse.Success(body = true)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun deleteFavoriteByAdId(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            favoriteAdsApi.deleteFavoriteByAdId(id = id)
            NetworkResponse.Success(body = true)
        }.getOrElse(networkErrorWrapper::handle)
    }
}