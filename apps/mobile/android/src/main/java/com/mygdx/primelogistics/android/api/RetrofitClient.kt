package com.mygdx.primelogistics.android.api
<<<<<<< HEAD

import okhttp3.OkHttpClient
import okhttp3.Request
=======
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

import okhttp3.OkHttpClient
import okhttp3.Request
=======
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======

import okhttp3.OkHttpClient
import okhttp3.Request
>>>>>>> a6baad7 (feat: create RetrofitClient object)
=======
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======

import okhttp3.OkHttpClient
import okhttp3.Request
>>>>>>> a6baad7 (feat: create RetrofitClient object)
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5085/"

    private var tokenProvider: (() -> String?)? = null

    fun init(provider: () -> String?) {
        this.tokenProvider = provider
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> a6baad7 (feat: create RetrofitClient object)
=======
>>>>>>> a6baad7 (feat: create RetrofitClient object)
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")

                tokenProvider?.invoke()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
<<<<<<< HEAD
=======
<<<<<<< HEAD
<<<<<<< HEAD
=======
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======
>>>>>>> a6baad7 (feat: create RetrofitClient object)
=======
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
>>>>>>> f20535f (feat: Create ApiService.kt and RetrofitClient.kt)
=======
>>>>>>> a6baad7 (feat: create RetrofitClient object)
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
