package com.example.garbagecollector.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.garbagecollector.api.dto.LoginJWTDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("JWT_TOKEN")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "AUTHORIZATION"
        )
    }


    val userTokenFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN] ?: ""
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: ""
    }

    suspend fun saveUserToken(loginJWTDto: LoginJWTDto) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = loginJWTDto.token.toString()
            preferences[USER_EMAIL] = loginJWTDto.email.toString()
        }
    }

    suspend fun clearUserCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences.remove(USER_EMAIL)
        }
    }
}