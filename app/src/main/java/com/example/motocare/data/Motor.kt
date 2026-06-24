package com.example.motocare.data

data class Motor(
    val id: Long = 0,
    val name: String,
    val plateNumber: String,
    val currentKilometer: Int,
    val isActive: Boolean = false
)
