package com.example.motocare.data

data class Pajak(
    val id: Long = 0,
    val motorId: Long,
    val taxType: String,
    val dueDate: String,
    val cost: Int,
    val status: String
)
