package com.example.mysavings.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RestData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val rest: Float = 0f
)
