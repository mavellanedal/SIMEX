package com.mygdx.primelogistics.android.api

import com.google.gson.annotations.SerializedName

data class Operation(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("OriginPortId")
    val originPortId: Int,
    @SerializedName("DestinationPortId")
    val destinationPortId: Int,
    @SerializedName("TotalCost")
    val totalCost: Double,
    @SerializedName("Etd")
    val etd: String?,
    @SerializedName("Eta")
    val eta: String?,
    @SerializedName("IncotermId")
    val incotermId: Int?,
    @SerializedName("PiecesNumber")
    val piecesNumber: Int?,
    @SerializedName("Kilograms")
    val kilograms: Double
)
