package com.dashkovskiy.api

import okhttp3.MultipartBody
import retrofit2.http.*

data class Ad(
    val id: Int,
    val author: Author,
    val title: String,
    val description: String,
    val createdAt: String,
    val price: Int,
    val currency: String,
    @ExcludeField
    val isFavorite : Boolean = false,
    val photos: List<AdPhoto>
)

data class UserAd(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val price: Int,
    val currency: String,
    val photos: List<AdPhoto>
)

data class Author(
    val id: Int,
    val name: String,
    val surname: String,
    val phone: String
)

data class AdPhoto(
    val id: Int,
    val imageUrl: String
)

data class UpdateUserAd(
    val title: String,
    val description: String,
    val price: String,
    val currency: String
)

interface AdsApi {
    @GET("ad/all")
    suspend fun getAds(@Query("page") page: Int): List<Ad>

    @GET("ad/{id}")
    suspend fun getAdById(@Path("id") id: Int) : Ad

    @GET("ad/search/{query}")
    suspend fun searchAds(@Path("query") query: String): List<Ad>

    @GET("user/ad/all")
    suspend fun getUserAds(): List<UserAd>

    @Multipart
    @POST("user/ad")
    suspend fun newUserAd(
        @Part("title") title : String,
        @Part("description") description: String,
        @Part("price") price : Int,
        @Part("currency") currency : String,
        @Part photos : List<MultipartBody.Part>
    )

    @PUT("user/ad/{id}")
    suspend fun updateUserAd(@Path("id") id: Int, @Body updateUserAd: UpdateUserAd)

    @DELETE("user/ad/{id}")
    suspend fun deleteUserAd(@Path("id") id: Int)
}