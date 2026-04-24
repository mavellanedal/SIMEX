package com.mygdx.primelogistics.android.models
<<<<<<< HEAD
=======

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gson.annotations.SerializedName

>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val rol: Role,
    val company: Company?,
    val username: String,
    val active: Boolean,
    val identificationCardPath: String?
<<<<<<< HEAD
=======
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
>>>>>>> b70ecef7239b91ae7ccaf9d3f344c8f26a51b981
)

