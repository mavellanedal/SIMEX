package com.mygdx.primelogistics.android.models

import android.R

data class User(
    val id : Int,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname : String,
    val rolId: Int,
    val companyId: Int,
    val username: String,
    val active : R.bool,
    val identificationCardPaths: String
)
