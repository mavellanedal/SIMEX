package com.mygdx.primelogistics.android.models
data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val rol: Role,
    val company: Company?,
    val username: String,
    val active: Boolean,
    val identificationCardPath: String?
)

