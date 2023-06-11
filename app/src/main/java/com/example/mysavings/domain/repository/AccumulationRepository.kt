package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.repository.Accumulation

interface AccumulationRepository {
    suspend fun getAccumulation(): MutableList<Accumulation>?

    suspend fun add(accumulation: Accumulation): Int

    suspend fun update(accumulation: Accumulation): Boolean

    suspend fun delete(accumulation: Accumulation): Boolean
}