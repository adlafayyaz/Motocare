package com.example.motocare.data

data class Bensin(
    val id: Long = 0,
    val motorId: Long,
    val fuelDate: String,
    val fuelType: String,
    val fuelBrand: String,
    val octane: String,
    val pricePerLiter: Int,
    val liter: Double,
    val cost: Int,
    val kilometer: Int
)
