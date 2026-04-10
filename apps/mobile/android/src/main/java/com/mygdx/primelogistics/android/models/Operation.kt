
package com.mygdx.primelogistics.android.models

import java.util.Date

data class Operation (
    val id: Int,
    val originPort: String,
    val destinationPort: String,
    val totalCost: Double,
    val etd: Date,
    val eta: Date,
    val IncotermId: Int,
    val pkgQuantity: Int,
    val totalWeight: Double
)
