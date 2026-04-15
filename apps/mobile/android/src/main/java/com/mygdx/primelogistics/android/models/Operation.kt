
package com.mygdx.primelogistics.android.models

data class Operation (
    val id: Int,
    val originPortId: Int,
    val destinationPortId: Int,
    val totalCost: Double,
    val etd: String,
    val eta: String,
    val incotermId: Int,
    val piecesNumber: Int,
    val kilograms: Double
)
