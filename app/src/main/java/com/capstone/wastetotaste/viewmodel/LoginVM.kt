package com.capstone.wastetotaste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.data.ResponseLogin
import com.capstone.wastetotaste.api.ApiConfig
import com.capstone.wastetotaste.data.RegisterUserData
import com.capstone.wastetotaste.data.ResponseRegister
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginVM(private val userPref: UserPreferencesManager) : ViewModel() {
    private val _isLoggingIn = MutableLiveData<Boolean>()
    val isLoggingIn: LiveData<Boolean> = _isLoggingIn
    var isErrorLogin: Boolean = false
    private val _msgLogin = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _msgLogin
    private val _authenticatedUser = MutableLiveData<ResponseLogin>()
    val authenticatedUser: LiveData<ResponseLogin> = _authenticatedUser

    var isErrorRegist: Boolean = false
    private val _isRegistering = MutableLiveData<Boolean>()
    val isRegistering: LiveData<Boolean> = _isRegistering
    private val _msgRegistration = MutableLiveData<String>()
    val registerStatus: LiveData<String> = _msgRegistration

    fun getResponseLogin(loginUserData: LoginUserData) {
        _isLoggingIn.value = true
        val api = ApiConfig.getApiService().loginUser(loginUserData)
        api.enqueue(object : retrofit2.Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoggingIn.value = false
                val responseBody = response.body()

                responseBody?.let {
                    // Simpan token dan nama pengguna ke preferensi
                    viewModelScope.launch {
                        userPref.storeName(it.loginResult.name)
                        userPref.storeToken(it.loginResult.token)
                    }
                }

                if (response.isSuccessful) {
                    isErrorLogin = false
                    _authenticatedUser.value = responseBody!!
                    _msgLogin.value = "Selamat Datang ${_authenticatedUser.value!!.loginResult.name}!"
                } else {
                    isErrorLogin = true
                    when (response.code()) {
                        401 -> _msgLogin.value =
                            "Email atau password yang anda masukan salah, silahkan coba lagi"
                        408 -> _msgLogin.value =
                            "Periksa koneksi internet anda dan coba lagi"
                        else -> _msgLogin.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                isErrorLogin = true
                _isLoggingIn.value = false
                _msgLogin.value = "Pesan error: " + t.message.toString()
            }

        })
    }

    fun getResponseRegister(registDataUser: RegisterUserData) {
        _isRegistering.value = true
        val api = ApiConfig.getApiService().registUser(registDataUser)
        api.enqueue(object : retrofit2.Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                _isRegistering.value = false
                if (response.isSuccessful) {
                    isErrorRegist = false
                    _msgRegistration.value = "Registrasi berhasil"
                } else {
                    isErrorRegist = true
                    when (response.code()) {
                        400 -> _msgRegistration.value =
                            "1"
                        408 -> _msgRegistration.value =
                            "Periksa koneksi internet Anda dan coba lagi"
                        else -> _msgRegistration.value = "Error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                isErrorRegist = true
                _isRegistering.value = false
                _msgRegistration.value = "Pesan error: " + t.message.toString()
            }

        })
    }
}