package com.mygdx.primelogistics.android.api
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

import com.mygdx.primelogistics.android.models.LoginRequest
import com.mygdx.primelogistics.android.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @GET("users")
    suspend fun getUsers(@Header("Authorization") authorization: String): Response<List<User>>
=======
=======

import com.mygdx.primelogistics.android.models.LoginRequest
>>>>>>> 2f61192 (feat: create api interface)
import com.mygdx.primelogistics.android.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @GET("users")
<<<<<<< HEAD
    suspend fun getUsers(): List<User>
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======
    suspend fun getUsers(@Header("Authorization") authorization: String): Response<List<User>>
>>>>>>> 2f61192 (feat: create api interface)
=======
import com.mygdx.primelogistics.android.models.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
}
