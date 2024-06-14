package com.capstone.wastetotaste.ui.authentication

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthSplashVM(private val userPref: UserPreferencesManager) : ViewModel() {

    fun getLoginStatus(): LiveData<Boolean> {
        return userPref.getLoginStatus().asLiveData()
    }

    fun storeLoginStatus(loginSession: Boolean) {
        viewModelScope.launch {
            userPref.storeLoginStatus(loginSession)
        }
    }


    fun storeToken(token: String) {
        viewModelScope.launch {
            userPref.storeToken(token)
        }
    }

    fun getName(): LiveData<String> {
        val user = FirebaseAuth.getInstance().currentUser
        val nameLiveData = MutableLiveData<String>()
        nameLiveData.value = user?.displayName
        return nameLiveData
    }

    fun storeName(token: String) {
        viewModelScope.launch {
            userPref.storeName(token)
        }
    }

    private fun resetLoginData() {
        viewModelScope.launch {
            userPref.resetLoginData()
        }
    }




    fun logout(context: Context) {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
//            userPref.resetLoginData()
            resetLoginData()
            Toast.makeText(context, R.string.logoutSucceed, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}