package com.capstone.wastetotaste

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.ui.settings.SettingsViewModel
import com.capstone.wastetotaste.ui.settings.ThemesSetting

class SettingViewModelFactory(private val themePref: ThemesSetting) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(themePref) as T
        }
        throw IllegalArgumentException("ViewModel class is not recognized: ${modelClass.name}")
    }
}