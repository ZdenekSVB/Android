package cz.pef.project.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Singleton DataStore definition
private val Context.dataStore by preferencesDataStore("user_preferences")

object UserPreferences {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val USERNAME = stringPreferencesKey("username")
}

class DataStoreManager @Inject constructor(private val context: Context) {

    // Save login state
    suspend fun saveLoginState(isLoggedIn: Boolean, userName: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferences.IS_LOGGED_IN] = isLoggedIn
            preferences[UserPreferences.USERNAME] = userName
        }
    }

    // Get login state
    fun getLoginState(): Flow<Pair<Boolean, String?>> {
        return context.dataStore.data.map { preferences ->
            val isLoggedIn = preferences[UserPreferences.IS_LOGGED_IN] ?: false
            val userName = preferences[UserPreferences.USERNAME]
            isLoggedIn to userName
        }
    }


}
