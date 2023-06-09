package com.dashkovskiy.api

import okhttp3.RequestBody
import retrofit2.http.*

interface ImageApi {

    @Headers("Content-Type: image/jpeg")
    @POST("user/ad/{id}/image")
    suspend fun newImage(@Path("id") id: Int, @Body bytes: RequestBody)

    @DELETE("image/{id}")
    suspend fun deleteImage(@Path("id") id: Int)
}