package com.mygdx.primelogistics.android.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("tokenType")
    val tokenType: String,
    val user: User
)

data class UpdateIdentificationCardPathRequest(
    val identificationCardPath: String
)
