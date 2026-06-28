package com.example.motocare.data

data class Servis(
    val id: Long = 0,
    val motorId: Long,
    val serviceDate: String,
    val serviceType: String,
    val kilometer: Int,
    val intervalKm: Int,
    val intervalMonth: Int,
    val cost: Int,
    val note: String = ""
)
