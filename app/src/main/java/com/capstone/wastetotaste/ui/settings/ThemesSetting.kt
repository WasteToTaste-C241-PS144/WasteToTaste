package com.capstone.wastetotaste.ui.settings
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ThemesSetting private constructor(private val dataStore: DataStore<Preferences>) {

    private val KEY_THEME = booleanPreferencesKey("themes_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_THEME] ?: false
        }
    }

    suspend fun saveThemeSetting(isNightThemeOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = isNightThemeOn
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemesSetting? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemesSetting {
            return INSTANCE ?: synchronized(this) {
                val newInstance = ThemesSetting(dataStore)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}