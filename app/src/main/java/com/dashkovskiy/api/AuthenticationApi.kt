package com.dashkovskiy.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class Login(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)

data class Register(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String
)

data class TokenPair(
    @SerializedName("access")
    val access: String,
    @SerializedName("refresh")
    val refresh: String
)

data class UserInfo(
    val name: String,
    val surname: String,
    val phone: String,
    val email: String?
)

interface AuthenticationApi {

    @POST("user/login")
    suspend fun login(@Body login: Login) : TokenPair

    @POST("user/register")
    suspend fun register(@Body register: Register): TokenPair

    @POST("user/refresh")
    suspend fun refresh(@Header("Authorization") token: String): Response<TokenPair>

    @GET("user/info")
    suspend fun userInfo() : UserInfo
}