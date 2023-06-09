package com.dashkovskiy.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class FavoriteAd(
    val id: Int,
    val ad: Ad
)

interface FavoriteAdsApi {
    @POST("user/favorite/{id}")
    suspend fun newFavorite(@Path("id") id: Int)

    @DELETE("user/favorite/{id}")
    suspend fun deleteFavorite(@Path("id") id: Int)

    @DELETE("user/ad/{id}/favorite")
    suspend fun deleteFavoriteByAdId(@Path("id") id: Int)

    @GET("user/favorites")
    suspend fun userFavorites(): List<FavoriteAd>
}