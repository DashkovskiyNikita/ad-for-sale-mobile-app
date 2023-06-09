package com.dashkovskiy


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PreferenceManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        private val USER_AUTH_STATE = booleanPreferencesKey("user_auth_state")
    }

    val accessTokenFlow = context.dataStore.data.map { it[ACCESS_TOKEN] ?: "" }

    val refreshTokenFlow = context.dataStore.data.map { it[REFRESH_TOKEN] ?: "" }

    val isUserAuthorizedFlow = context.dataStore.data.map { it[USER_AUTH_STATE] ?: false }

    suspend fun setIsUserAuthorized(isAuthorized : Boolean){
        context.dataStore.edit {
            it[USER_AUTH_STATE] = isAuthorized
        }
    }

    suspend fun setAccessToken(accessToken: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        context.dataStore.edit {
            it[REFRESH_TOKEN] = refreshToken
        }
    }
}