package com.example.mysavings.domain.models.repository

data class Accumulation(
    var id: Int,
    var name: String = "",
    var sum: Float = 0f
)
