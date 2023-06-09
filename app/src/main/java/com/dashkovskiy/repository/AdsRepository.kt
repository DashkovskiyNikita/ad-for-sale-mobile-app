package com.dashkovskiy.repository

import com.dashkovskiy.api.AdsApi
import com.dashkovskiy.api.NetworkErrorWrapper
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.api.UpdateUserAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AdsRepository(
    private val adsApi: AdsApi,
    private val networkErrorWrapper: NetworkErrorWrapper,
) {
    suspend fun getAds(page: Int) = withContext(Dispatchers.IO) {
        runCatching {
            val ads = adsApi.getAds(page = page)
            NetworkResponse.Success(body = ads)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun getAdById(id : Int) = withContext(Dispatchers.IO) {
        runCatching {
            val ad = adsApi.getAdById(id = id)
            NetworkResponse.Success(body = ad)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun searchAds(query: String) = withContext(Dispatchers.IO) {
        runCatching {
            val ads = adsApi.searchAds(query = query)
            NetworkResponse.Success(body = ads)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun getUserAds() = withContext(Dispatchers.IO) {
        runCatching {
            val userAds = adsApi.getUserAds()
            NetworkResponse.Success(body = userAds)
        }.getOrElse(networkErrorWrapper::handle)
    }

    suspend fun newUserAd(userAd: UpdateUserAd, photos: List<ByteArray>) =
        withContext(Dispatchers.IO) {
            runCatching {
                adsApi.newUserAd(
                    title = userAd.title,
                    description = userAd.description,
                    price = userAd.price.toInt(),
                    currency = userAd.currency,
                    photos = photos.map { MultipartBody.Part.create(it.toRequestBody()) }
                )
                NetworkResponse.Success(body = true)
            }.getOrElse(networkErrorWrapper::handle)
        }

    suspend fun updateUserAd(id: Int, updateUserAd: UpdateUserAd) =
        withContext(Dispatchers.IO) {
            runCatching {
                adsApi.updateUserAd(id = id, updateUserAd = updateUserAd)
                NetworkResponse.Success(body = true)
            }.getOrElse(networkErrorWrapper::handle)
        }

    suspend fun deleteUserAd(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            adsApi.deleteUserAd(id = id)
            NetworkResponse.Success(body = true)
        }.getOrElse(networkErrorWrapper::handle)
    }
}