package cz.mendelu.pef.xsvobo.projekt.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "set_image")

class SetPreferences(private val context: Context) {

    companion object {
        val USER_ID_KEY = longPreferencesKey("set_id")
        fun profileImageUrlKey(setId: Long) = stringPreferencesKey("profile_image_url_$setId")
    }

    suspend fun saveSetId(setId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = setId
        }
    }

    val setId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    suspend fun saveIconUrl(setId: Long, profileImageUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[profileImageUrlKey(setId)] = profileImageUrl
        }
    }

    fun getIconUrl(setId: Long): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[profileImageUrlKey(setId)]
    }

    suspend fun clearIconUrl(setId: Long) {
        context.dataStore.edit { preferences ->
            preferences.remove(profileImageUrlKey(setId))
        }
    }
}
