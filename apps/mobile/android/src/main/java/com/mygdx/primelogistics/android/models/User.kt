package com.mygdx.primelogistics.android.models

<<<<<<< HEAD
<<<<<<< HEAD
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
=======
import android.R
=======
import com.google.gson.annotations.SerializedName
>>>>>>> c4ce83d (feat: create model)

data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val rol: Role,
    val company: Company?,
    val username: String,
<<<<<<< HEAD
    val active : R.bool,
    val identificationCardPaths: String
>>>>>>> 988cd88 (feat: create user.kt)
=======
    val active: Boolean,
    @SerializedName("identification_card_path")
    val identificationCardPath: String?
>>>>>>> c4ce83d (feat: create model)
)
