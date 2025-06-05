package org.courselab.app.data

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

/**
 * Creates a DataStore instance for storing preferences.
 *
 * For more information on using DataStore in Kotlin Multiplatform, refer to:
 * [Local Preferences in Kotlin Multiplatform using DataStore](https://medium.com/arconsis/jetpack-preferences-datastore-in-kotlin-multiplatform-mobile-kmm-6bf046772217)
 * @param producePath A function that returns the path to the DataStore file.
 */

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { producePath().toPath() }
    )

const val PREFERENCES_DB = "cl_user.preferences_pb"



val USER_ID = intPreferencesKey("id")
val IS_FIRST_LOGIN = booleanPreferencesKey("is_first_login")
val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
val SESSION_TOKEN = stringPreferencesKey("session_token")
val USER_EMAIL = stringPreferencesKey("email")
val THEME_PREFERENCE = stringPreferencesKey("theme_preference") // "light", "dark", "system"
val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
val LANGUAGE_PREFERENCE = stringPreferencesKey("language_preference") // "en", "es", "fr"
val LAST_SYNC_TIMESTAMP = longPreferencesKey("last_sync_timestamp")



class UserPreferencesDataStore(private val dataStore: DataStore<Preferences>) {

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    suspend fun setUserId(id: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }

    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    suspend fun setUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    val isFirstLogin: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_LOGIN]
    }

    suspend fun setIsFirstLogin(isFirst: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_LOGIN] = isFirst
        }
    }

    val isLoggedIn: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN]
    }

    suspend fun setIsLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    val sessionToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN]
    }

    suspend fun setSessionToken(token: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOKEN] = token
        }
    }

    val themePreference: Flow<String?> = dataStore.data.map { preferences ->
        preferences[THEME_PREFERENCE]
    }

    suspend fun setThemePreference(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE] = theme
        }
    }

    val notificationEnabled: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[NOTIFICATION_ENABLED]
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = enabled
        }
    }

    val languagePreference: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_PREFERENCE]
    }

    suspend fun setLanguagePreference(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_PREFERENCE] = language
        }
    }

    val lastSyncTimestamp: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[LAST_SYNC_TIMESTAMP]
    }

    suspend fun setLastSyncTimestamp(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIMESTAMP] = timestamp
        }
    }
}