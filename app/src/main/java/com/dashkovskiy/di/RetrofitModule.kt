package com.dashkovskiy.di

import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.*
import com.dashkovskiy.utils.Constants
import com.google.gson.ExclusionStrategy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

    singleOf(::JwtAuthenticator).bind<Authenticator>()

    singleOf(::NetworkErrorWrapper)

    singleOf(::ExclusionStrategyImpl).bind<ExclusionStrategy>()

    single {
        GsonBuilder()
            .setExclusionStrategies(get())
            .create()
    }

    single {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = AuthInterceptor(get())

        val okHttpClient = OkHttpClient.Builder()
            //.authenticator(get())
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(okHttpClient)
            .build()
    }

    single {
        get<Retrofit>().create(AdsApi::class.java)
    }
    single {
        get<Retrofit>().create(AuthenticationApi::class.java)
    }
    single {
        get<Retrofit>().create(FavoriteAdsApi::class.java)
    }
    single {
        get<Retrofit>().create(ImageApi::class.java)
    }
}

class AuthInterceptor(
    private val preferenceManager: PreferenceManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            preferenceManager.accessTokenFlow.first()
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}

class JwtAuthenticator(
    private val preferenceManager: PreferenceManager
) : Authenticator, KoinComponent {

    private val authenticationApi: AuthenticationApi by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = preferenceManager.refreshTokenFlow.first()
            val tokenPairResponse = authenticationApi.refresh(token = refreshToken)

            if (!tokenPairResponse.isSuccessful) {
                preferenceManager.setIsUserAuthorized(false)
                return@runBlocking null
            }

            val tokenPair = tokenPairResponse.body() ?: return@runBlocking null

            with(preferenceManager) {
                setAccessToken(tokenPair.access)
                setRefreshToken(tokenPair.refresh)
                setIsUserAuthorized(true)
            }

            response
                .request
                .newBuilder()
                .addHeader("Authorization", "Bearer ${tokenPair.access}")
                .build()
        }
    }
}