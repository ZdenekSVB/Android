package cz.pef.project.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.pef.project.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("user_preferences")

object UserPreferences {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val USERNAME = stringPreferencesKey("username")
    val DARK_MODE = booleanPreferencesKey("dark_mode") // Přidání klíče pro dark mód
}

class DataStoreManager @Inject constructor(
    private val context: Context,
    private val userDao: UserDao
) {
    suspend fun getUserId(userName: String): Int {
        return userDao.getUserIdByUsername(userName) ?: throw IllegalArgumentException("User not found")
    }

    suspend fun saveLoginState(isLoggedIn: Boolean, userName: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferences.IS_LOGGED_IN] = isLoggedIn
            preferences[UserPreferences.USERNAME] = userName
        }
    }

    fun getLoginState(): Flow<Pair<Boolean, String?>> {
        return context.dataStore.data.map { preferences ->
            val isLoggedIn = preferences[UserPreferences.IS_LOGGED_IN] ?: false
            val userName = preferences[UserPreferences.USERNAME]
            isLoggedIn to userName
        }
    }

    // Uložení nastavení Dark Mode
    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferences.DARK_MODE] = isDarkMode
        }
    }

    // Načítání stavu Dark Mode
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UserPreferences.DARK_MODE] ?: false // Defaultně Light Mode
    }
}
