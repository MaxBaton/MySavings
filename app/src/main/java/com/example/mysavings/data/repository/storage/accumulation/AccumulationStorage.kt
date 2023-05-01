package com.example.mysavings.data.repository.storage.accumulation

import com.example.mysavings.data.models.db.AccumulationData

interface AccumulationStorage {
    suspend fun getAccumulation(): MutableList<AccumulationData>?

    suspend fun add(accumulationData: AccumulationData): Boolean

    suspend fun update(accumulationData: AccumulationData): Boolean

    suspend fun delete(accumulationData: AccumulationData): Boolean
}