package com.example.motocare.data

data class Oli(
    val id: Long = 0,
    val motorId: Long,
    val oilChangeDate: String,
    val kilometer: Int,
    val nextKilometer: Int,
    val intervalKm: Int,
    val intervalMonth: Int,
    val oilType: String,
    val cost: Int
)
