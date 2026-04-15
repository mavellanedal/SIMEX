package com.mygdx.primelogistics.android.api

import com.mygdx.primelogistics.android.models.LoginRequest
import com.mygdx.primelogistics.android.models.LoginResponse
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/auth/me")
    suspend fun getMe(): Response<User>

    @GET("api/operations/user-operations")
    suspend fun getUserOperations(): Response<List<Operation>>

    @GET("api/operations/recent")
    suspend fun getRecentUserOperations(): Response<List<Operation>>
}
