package com.example.mysavings.domain.models.repository

data class Expenditure(
    val id: Int = 0,
    var date: String,
    var sum: Float = 0f,
    var description: String = ""
)