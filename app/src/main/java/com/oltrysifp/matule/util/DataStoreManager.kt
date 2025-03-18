package com.oltrysifp.matule.util

import android.app.Notification
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oltrysifp.matule.models.ProductRecord
import com.oltrysifp.matule.models.User
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("prefs")

class DataStoreManager(private val context: Context) {
    suspend fun saveUser(user: User) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("user")] = Json.encodeToString(user)
        }
    }

    fun getUser() = context.dataStore.data.map { pref ->
        val user = pref[stringPreferencesKey("user")]

        if (user != null) {
            try {
                return@map Json.decodeFromString<User>(user)
            } catch (e: Exception) {
                return@map null
            }
        } else {
            return@map null
        }
    }
}