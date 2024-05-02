package br.com.jwar.triviachallenge.data.datasources.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.map

private const val USER_XP = "UserXP"
private const val USER_PREFERENCES_DATA_STORE_NAME = "user_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_DATA_STORE_NAME)

class UserPreferences @Inject constructor(context: Context) {
    private val dataStore = context.dataStore

    suspend fun addXp(xp: Int) {
        dataStore.edit { preferences ->
            val currentXP = preferences[intPreferencesKey(USER_XP)] ?: 0
            preferences[intPreferencesKey(USER_XP)] = currentXP + xp
        }
    }

    fun getXp() = dataStore.data.map { preferences ->
        preferences[intPreferencesKey(USER_XP)] ?: 0
    }
}