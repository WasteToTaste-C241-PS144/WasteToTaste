package com.capstone.wastetotaste.api
import com.capstone.wastetotaste.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
//    @POST("register")
//    fun registUser(@Body requestRegister: RegisterUserData): Call<ResponseRegister>
//
//    @POST("login")
//    fun loginUser(@Body requestLogin: LoginUserData): Call<ResponseLogin>

    @POST("register")
    fun registerUser(@Body requestRegister: RegisterUserData): Call<RegisterResponse>

    @GET("login")
    fun loginUser(@Body requestLogin: LoginUserData): Call<LoginResponse>

//    @POST("logout")
//    fun logoutUser(@Body requestLogout: LogoutRequestData): Call<ResponseLogout>
//
//    @POST("token/refresh")
//    fun refreshToken(@Body requestRefresh: RefreshTokenData): Call<ResponseRefreshToken>


}
