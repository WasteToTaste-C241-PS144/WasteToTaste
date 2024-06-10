package com.capstone.wastetotaste.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel (private val themePref: ThemesSetting) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return themePref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isNightThemeOn: Boolean) {
        viewModelScope.launch {
            themePref.saveThemeSetting(isNightThemeOn)
        }
    }
}