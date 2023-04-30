package com.example.mysavings.data.mappers

import com.example.mysavings.data.models.RestData
import com.example.mysavings.domain.models.Rest

fun Rest.mapToRestData(): RestData {
    return RestData(
        id = this.id,
        rest = this.rest
    )
}

fun List<Rest>.mapToListRestData(): List<RestData> {
    val list = mutableListOf<RestData>()
    this.forEach { rest ->
        val restData = rest.mapToRestData()
        list.add(restData)
    }

    return list
}

fun RestData.mapToRest(): Rest {
    return Rest(
        id = this.id,
        rest = this.rest
    )
}

fun List<RestData>.mapToListRest(): List<Rest> {
    val list = mutableListOf<Rest>()
    this.forEach { restData ->
        val rest = restData.mapToRest()
        list.add(rest)
    }

    return list
}