package com.example.mysavings.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenditureData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: String,
    var sum: Float = 0f,
    var text: String = ""
)
