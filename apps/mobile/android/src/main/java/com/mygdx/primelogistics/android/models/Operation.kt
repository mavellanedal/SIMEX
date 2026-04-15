
package com.mygdx.primelogistics.android.models

data class Operation (
    val id: Int,
    val orderReference: String,
    val originPortName: String,
    val destinationPortName: String,
    val totalCost: Double,
    val etd: String,
    val eta: String,
    val incotermId: Int?,
    val piecesNumber: Int?,
    val kilograms: Double
)
