package com.dashkovskiy.repository

import com.dashkovskiy.api.ImageApi
import com.dashkovskiy.api.NetworkErrorWrapper
import com.dashkovskiy.api.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ImageRepository(
    private val imageApi: ImageApi,
    private val networkErrorWrapper: NetworkErrorWrapper
) {
    suspend fun newImage(id: Int, bytes: ByteArray) =
        withContext(Dispatchers.IO) {
            runCatching {
                val body = bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                imageApi.newImage(id = id, bytes = body)
                NetworkResponse.Success(body = true)
            }.getOrElse(networkErrorWrapper::handle)
        }

    suspend fun deleteImage(id: Int) = withContext(Dispatchers.IO) {
        runCatching {
            imageApi.deleteImage(id = id)
            NetworkResponse.Success(body = true)
        }.getOrElse(networkErrorWrapper::handle)
    }
}