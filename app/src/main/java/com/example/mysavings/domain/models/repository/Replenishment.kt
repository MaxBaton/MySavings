package com.example.mysavings.domain.models.repository

data class Replenishment(
    val id: Int = 0,
    var sum: Float,
    val date: String,
    val idAccumulation: Int = 0
)