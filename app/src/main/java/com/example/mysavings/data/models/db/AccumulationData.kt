package com.example.mysavings.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccumulationData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String = "",
    var sum: Float = 0f
)
