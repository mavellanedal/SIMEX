package com.mygdx.primelogistics.android.api

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
}
