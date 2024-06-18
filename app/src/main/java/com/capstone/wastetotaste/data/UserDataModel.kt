package com.capstone.wastetotaste.data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class RegisterUserData(
    var name: String,
    var email: String,
    var password: String
)

data class LoginUserData(
    var email: String,
    var password: String
)


@Parcelize
data class RegisterResponse(
    val token: String
) : Parcelable

@Parcelize
data class LoginResult(
    val name: String,
    val token: String,

    ): Parcelable

@Parcelize
data class LoginResponse(
    val token: String,
    val loginResult: LoginResult
) : Parcelable


