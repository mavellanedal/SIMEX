
package com.mygdx.primelogistics.android.models

data class Operation (
    val id: Int,
    val orderReference: String,
    val originPortName: String,
    val destinationPortName: String,
    val totalCost: Double,
    val etd: String,
    val eta: String,
    val incotermCode: String,
    val piecesNumber: Int?,
    val kilograms: Double,
    val statusName: String?,
    val trackingFlowId: Int?,
    val trackingFlowName: String?,
    val currentTrackingFlowStepId: Int?,
    val currentTrackingStepName: String?,
    val currentTrackingStepOrder: Int?,
    val currentTrackingStepUiPercent: Int?,
    val currentTrackingStepArrivedAt: String?
)
