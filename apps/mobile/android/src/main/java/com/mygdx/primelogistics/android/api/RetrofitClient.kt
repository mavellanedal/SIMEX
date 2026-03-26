package com.mygdx.primelogistics.android.api
<<<<<<< HEAD

import okhttp3.OkHttpClient
import okhttp3.Request
=======
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

<<<<<<< HEAD
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
=======
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
