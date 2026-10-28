package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val price: Double,
    val availableSeats: Int,
    val imageUrl: String? = null
)