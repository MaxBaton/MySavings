package com.example.mysavings.data.models.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys =
[ForeignKey(
    entity = AccumulationData::class, parentColumns = ["id"],
    childColumns = ["idAccumulation"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE
)]
)
data class ReplenishmentData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var sum: Float,
    val date: String,
    val idAccumulation: Int = 0
)