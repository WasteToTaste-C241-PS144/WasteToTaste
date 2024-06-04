package com.capstone.wastetotaste.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.UserPreferencesManager

class LoginVMFactory(private val userPref: UserPreferencesManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginVM(userPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
