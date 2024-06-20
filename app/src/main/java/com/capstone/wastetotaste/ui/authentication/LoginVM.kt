package com.capstone.wastetotaste.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.data.LoginResponse
import com.capstone.wastetotaste.api.ApiConfig

import com.capstone.wastetotaste.data.RegisterUserData
import com.capstone.wastetotaste.data.RegisterResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginVM(private val userPref: UserPreferencesManager) : ViewModel() {
//    private val _isLoggingIn = MutableLiveData<Boolean>()
//    val isLoggingIn: LiveData<Boolean> = _isLoggingIn
//    var isErrorLogin: Boolean = false
//    private val _msgLogin = MutableLiveData<String>()
//    val loginStatus: LiveData<String> = _msgLogin
//    private val _authenticatedUser = MutableLiveData<LoginResponse>()
//    val authenticatedUser: LiveData<LoginResponse> = _authenticatedUser
//
//    var isErrorRegist: Boolean = false
//    private val _isRegistering = MutableLiveData<Boolean>()
//    val isRegistering: LiveData<Boolean> = _isRegistering
//    private val _msgRegistration = MutableLiveData<String>()
//    val registerStatus: LiveData<String> = _msgRegistration
//    private val _loading = MutableLiveData<Boolean>()
    ////

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _registrationResult = MutableLiveData<Result<String>>()
    val registrationResult: LiveData<Result<String>> get() = _registrationResult

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun registerUser(name: String, email: String, password: String) {
        _loading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            _loading.value = false
                            if (updateTask.isSuccessful) {
                                _registrationResult.value = Result.success("Registrasi berhasil!")
                                loginUser(email, password)
                            } else {
                                _registrationResult.value = Result.failure(Exception("Failed to update profile."))
                            }
                        }
                } else {
                    _loading.value = false
                    _registrationResult.value = Result.failure(task.exception ?: Exception("Registrasi gagal"))
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        _loading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        val user = auth.currentUser
                        userPref.storeLoginStatus(true)
                        userPref.storeToken(user?.uid ?: "")
                        userPref.storeName(user?.displayName ?: "")
                        _registrationResult.value = Result.success("Login berhasil!")
                    }
                } else {
                    _registrationResult.value = Result.failure(task.exception ?: Exception("Login gagal"))
                }
            }
    }
}
