package com.example.mysavings.domain.models

data class Expenditure(
    var id: Int,
    var date: String,
    var sum: Float = 0f,
    var text: String = ""
)