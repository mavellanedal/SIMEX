package com.mygdx.primelogistics.android.api
<<<<<<< HEAD
=======
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981

import com.mygdx.primelogistics.android.models.LoginRequest
import com.mygdx.primelogistics.android.models.LoginResponse
import com.mygdx.primelogistics.android.models.UpdateIdentificationCardPathRequest
import com.mygdx.primelogistics.android.models.Operation
import com.mygdx.primelogistics.android.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

<<<<<<< HEAD
    @GET("api/auth/me")
    suspend fun getMe(): Response<User>
=======
    @GET("users")
    suspend fun getUsers(@Header("Authorization") authorization: String): Response<List<User>>
=======
=======
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981

    @PUT("api/auth/id-card-path")
    suspend fun updateIdentificationCardPath(
        @Header("Authorization") authorization: String,
        @Body request: UpdateIdentificationCardPathRequest
    ): Response<ResponseBody>

    @GET("api/operations/user-operations")
    suspend fun getUserOperations(): Response<List<Operation>>

<<<<<<< HEAD
    @GET("api/operations/recent")
    suspend fun getRecentUserOperations(): Response<List<Operation>>
=======
    @GET("users")
<<<<<<< HEAD
    suspend fun getUsers(): List<User>
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======
    suspend fun getUsers(@Header("Authorization") authorization: String): Response<List<User>>
>>>>>>> 2f61192 (feat: create api interface)
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
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
}
