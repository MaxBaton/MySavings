package com.example.mysavings.data.mappers

import com.example.mysavings.data.models.db.AccumulationData
import com.example.mysavings.domain.models.Accumulation

fun Accumulation.mapToAccumulationData(): AccumulationData {
    return AccumulationData(
        id = this.id,
        name = this.name,
        sum = this.sum
    )
}

fun List<Accumulation>.mapToListAccumulationData(): List<AccumulationData> {
    val list = mutableListOf<AccumulationData>()

    this.forEach { accumulation ->
        val accumulationData = accumulation.mapToAccumulationData()
        list.add(accumulationData)
    }

    return list
}

fun AccumulationData.mapToAccumulation(): Accumulation {
    return Accumulation(
        id = this.id,
        name = this.name,
        sum = this.sum
    )
}

fun List<AccumulationData>.mapToListAccumulation(): List<Accumulation> {
    val list = mutableListOf<Accumulation>()

    this.forEach { accumulationData ->
        val accumulation = accumulationData.mapToAccumulation()
        list.add(accumulation)
    }

    return list
}