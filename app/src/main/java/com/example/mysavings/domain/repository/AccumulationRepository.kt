package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.Accumulation

interface AccumulationRepository {
    suspend fun getAccumulation(): MutableList<Accumulation>?

    suspend fun add(accumulation: Accumulation): Boolean

    suspend fun update(accumulation: Accumulation): Boolean

    suspend fun delete(accumulation: Accumulation): Boolean
}