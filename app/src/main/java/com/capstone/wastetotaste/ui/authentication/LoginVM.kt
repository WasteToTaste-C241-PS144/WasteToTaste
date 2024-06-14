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
                                _registrationResult.value = Result.success("Registration successful!")
                                loginUser(email, password)
                            } else {
                                _registrationResult.value = Result.failure(Exception("Failed to update profile."))
                            }
                        }
                } else {
                    _loading.value = false
                    _registrationResult.value = Result.failure(task.exception ?: Exception("Registration failed"))
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
                        _registrationResult.value = Result.success("Login successful!")
                    }
                } else {
                    _registrationResult.value = Result.failure(task.exception ?: Exception("Login failed"))
                }
            }
    }
}

//    fun getResponseLogin(loginUserData: LoginUserData) {
//        _isLoggingIn.value = true
//        val api = ApiConfig.getApiService().loginUser(loginUserData)
//        api.enqueue(object : retrofit2.Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                _isLoggingIn.value = false
//                val responseBody = response.body()
//
//                responseBody?.let {
//                    // Simpan token dan nama pengguna ke preferensi
//                    viewModelScope.launch {
//                        userPref.storeName(it.loginResult.name)
//                        userPref.storeToken(it.loginResult.token)
//                    }
//                }
//
//                if (response.isSuccessful) {
//                    isErrorLogin = false
//                    _authenticatedUser.value = responseBody!!
//                    _msgLogin.value = "Selamat Datang ${_authenticatedUser.value!!.loginResult.name}!"
//                } else {
//                    isErrorLogin = true
//                    when (response.code()) {
//                        401 -> _msgLogin.value =
//                            "Email atau password yang anda masukan salah, silahkan coba lagi"
//                        408 -> _msgLogin.value =
//                            "Periksa koneksi internet anda dan coba lagi"
//                        else -> _msgLogin.value = "Pesan error: " + response.message()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                isErrorLogin = true
//                _isLoggingIn.value = false
//                _msgLogin.value = "Pesan error: " + t.message.toString()
//            }
//
//        })
//    }
//
//    fun getResponseRegister(registDataUser: RegisterUserData) {
//        _isRegistering.value = true
//        val api = ApiConfig.getApiService().registerUser(registDataUser)
//        api.enqueue(object : retrofit2.Callback<RegisterResponse> {
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                _isRegistering.value = false
//                if (response.isSuccessful) {
//                    isErrorRegist = false
//                    _msgRegistration.value = "Registrasi berhasil"
//                } else {
//                    isErrorRegist = true
//                    when (response.code()) {
//                        200 -> _msgRegistration.value =
//                            "1"
//                        408 -> _msgRegistration.value =
//                            "Periksa koneksi internet Anda dan coba lagi"
//                        else -> _msgRegistration.value = "Error: " + response.message()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                isErrorRegist = true
//                _isRegistering.value = false
//                _msgRegistration.value = "Pesan error: " + t.message.toString()
//            }
//
//        })
//    }
