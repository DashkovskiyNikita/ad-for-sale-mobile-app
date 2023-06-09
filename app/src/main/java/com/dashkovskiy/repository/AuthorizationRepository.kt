package com.dashkovskiy.repository

import com.dashkovskiy.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorizationRepository(
    private val authenticationApi: AuthenticationApi,
    private val networkErrorWrapper: NetworkErrorWrapper
) {

    suspend fun login(login: String, password: String): NetworkResponse<TokenPair, Nothing> =
        withContext(Dispatchers.IO) {
            runCatching {
                val loginModel = Login(login = login, password = password)
                val tokenPair = authenticationApi.login(login = loginModel)
                NetworkResponse.Success(body = tokenPair)
            }.getOrElse(networkErrorWrapper::handle)
        }

    suspend fun register(register: Register): NetworkResponse<TokenPair, Nothing> =
        withContext(Dispatchers.IO) {
            runCatching {
                val tokenPair = authenticationApi.register(register = register)
                NetworkResponse.Success(body = tokenPair)
            }.getOrElse(networkErrorWrapper::handle)
        }


    suspend fun userInfo(): NetworkResponse<UserInfo, Nothing> =
        withContext(Dispatchers.IO) {
            runCatching {
                val userInfo = authenticationApi.userInfo()
                NetworkResponse.Success(body = userInfo)
            }.getOrElse(networkErrorWrapper::handle)
        }

}