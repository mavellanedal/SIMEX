package com.mygdx.primelogistics.android.api

import com.google.gson.annotations.SerializedName

data class Operation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("orderReference")
    val orderReference: String,
    @SerializedName("originPortId")
    val originPortId: Int,
    @SerializedName("destinationPortId")
    val destinationPortId: Int,
    @SerializedName("totalCost")
    val totalCost: Double,
    @SerializedName("etd")
    val etd: String?,
    @SerializedName("eta")
    val eta: String?,
    @SerializedName("incotermId")
    val incotermId: Int?,
    @SerializedName("piecesNumber")
    val piecesNumber: Int?,
    @SerializedName("kilograms")
    val kilograms: Double
)
