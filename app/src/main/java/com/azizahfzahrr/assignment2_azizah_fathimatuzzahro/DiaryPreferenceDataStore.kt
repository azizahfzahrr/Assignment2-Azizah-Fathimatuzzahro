package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstant.DATA_STORE_NAME)

class DiaryPreferenceDataStore private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setUserLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_IS_LOGGED_IN] = isLoggedIn
        }
    }
    suspend fun getUserLoginStatus(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_IS_LOGGED_IN] ?: false
        }.first()
    }
    suspend fun setSortDiary(sortDiary: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_SORT_DIARY] = sortDiary
        }
    }
    val sortDiary: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.KEY_SORT_DIARY] ?: true
        }
    suspend fun setUserRegistered(isRegistered: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_IS_REGISTERED] = isRegistered
        }
    }
    suspend fun isUserRegistered(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_IS_REGISTERED] ?: false
        }.first()
    }
    suspend fun setLoggedInEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_LOGGED_IN_EMAIL] = email
        }
    }
    suspend fun getLoggedInEmail(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_LOGGED_IN_EMAIL]
        }.first()
    }
    suspend fun setUserRegistrationStatus(isRegistered: Boolean, email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_IS_REGISTERED] = isRegistered
            preferences[PreferencesKeys.KEY_LOGGED_IN_EMAIL] = email
        }
    }

    private object PreferencesKeys {
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("key_is_logged_in")
        val KEY_SORT_DIARY = booleanPreferencesKey(DataStoreConstant.KEY_SORT_DIARY)
        val KEY_IS_REGISTERED = booleanPreferencesKey("key_is_registered")
        val KEY_LOGGED_IN_EMAIL = stringPreferencesKey("key_logged_in_email")
    }

    companion object {
        @Volatile
        private var INSTANCE: DiaryPreferenceDataStore? = null

        fun getInstance(context: Context): DiaryPreferenceDataStore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DiaryPreferenceDataStore(context.dataStore).also { INSTANCE = it }
            }
        }
    }
}