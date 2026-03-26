package com.mygdx.primelogistics.android.api
import com.mygdx.primelogistics.android.models.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
