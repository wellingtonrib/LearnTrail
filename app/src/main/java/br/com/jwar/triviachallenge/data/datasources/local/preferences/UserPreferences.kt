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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveXP(xp: Int) {
        dataStore.edit { preferences ->
            val currentXP = preferences[intPreferencesKey(USER_XP)] ?: 0
            preferences[intPreferencesKey(USER_XP)] = currentXP + xp
        }
    }

    fun getXP() = dataStore.data.map { preferences ->
        preferences[intPreferencesKey(USER_XP)] ?: 0
    }
}