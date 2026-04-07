package com.mygdx.primelogistics.android.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val rol: Role,
    val company: Company?,
    val username: String,
    val active: Boolean,
    @SerializedName("identification_card_path")
    val identificationCardPath: String?
)
