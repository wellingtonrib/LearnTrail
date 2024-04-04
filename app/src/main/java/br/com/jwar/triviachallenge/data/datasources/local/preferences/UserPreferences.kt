package br.com.jwar.triviachallenge.data.datasources.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

const val USER_XP = "UserXP"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(context: Context) {
    private val dataStore = context.dataStore

    fun saveXP(xp: Int) {
        runBlocking {
            dataStore.edit { preferences ->
                val currentXP = preferences[intPreferencesKey(USER_XP)] ?: 0
                preferences[intPreferencesKey(USER_XP)] = currentXP + xp
            }
        }
    }

    fun getXP(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(USER_XP)] ?: 0
        }
    }
}