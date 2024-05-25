package br.com.jwar.learntrail.data.datasources.local.preferences.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences as CorePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.jwar.learntrail.data.datasources.local.preferences.Preferences
import javax.inject.Inject
import kotlinx.coroutines.flow.map

private const val USER_XP = "UserXP"
private const val USER_PREFERENCES_DATA_STORE_NAME = "user_preferences"

private val Context.dataStore: DataStore<CorePreferences> by preferencesDataStore(name = USER_PREFERENCES_DATA_STORE_NAME)

class DataStorePreferences @Inject constructor(context: Context):
    Preferences {
    private val dataStore = context.dataStore

    override suspend fun addXp(xp: Int) {
        dataStore.edit { preferences ->
            val currentXP = preferences[intPreferencesKey(USER_XP)] ?: 0
            preferences[intPreferencesKey(USER_XP)] = currentXP + xp
        }
    }

    override fun getXp() = dataStore.data.map { preferences ->
        preferences[intPreferencesKey(USER_XP)] ?: 0
    }
}