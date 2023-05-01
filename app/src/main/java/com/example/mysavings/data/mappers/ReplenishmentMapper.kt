package com.example.mysavings.data.mappers

import com.example.mysavings.data.models.db.ReplenishmentData
import com.example.mysavings.domain.models.Replenishment

fun Replenishment.mapToReplenishmentData(): ReplenishmentData {
    return ReplenishmentData(
        id = this.id,
        sum = this.sum,
        date = this.date,
        idAccumulation = this.idAccumulation
    )
}

fun List<Replenishment>.mapToListReplenishmentData(): List<ReplenishmentData> {
    val list = mutableListOf<ReplenishmentData>()

    this.forEach { replenishment ->
        val replenishmentData = replenishment.mapToReplenishmentData()
        list.add(replenishmentData)
    }

    return list
}

fun ReplenishmentData.mapToReplenishment(): Replenishment {
    return Replenishment(
        id = this.id,
        sum = this.sum,
        date = this.date,
        idAccumulation = this.idAccumulation
    )
}

fun List<ReplenishmentData>.mapToListReplenishment(): List<Replenishment> {
    val list = mutableListOf<Replenishment>()

    this.forEach { replenishmentData ->
        val replenishment = replenishmentData.mapToReplenishment()
        list.add(replenishment)
    }

    return list
}