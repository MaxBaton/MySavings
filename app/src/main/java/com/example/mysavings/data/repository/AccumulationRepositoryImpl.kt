package com.example.mysavings.data.repository

import com.example.mysavings.data.mappers.mapToAccumulationData
import com.example.mysavings.data.mappers.mapToListAccumulation
import com.example.mysavings.data.repository.storage.accumulation.AccumulationStorage
import com.example.mysavings.domain.models.repository.Accumulation
import com.example.mysavings.domain.repository.AccumulationRepository

class AccumulationRepositoryImpl(private val accumulationStorage: AccumulationStorage): AccumulationRepository {
    override suspend fun getAccumulation(): MutableList<Accumulation>? {
        return try {
            accumulationStorage.getAccumulation()?.mapToListAccumulation()?.toMutableList()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(accumulation: Accumulation): Boolean {
        return try {
            val accumulationData = accumulation.mapToAccumulationData()
            accumulationStorage.add(accumulationData = accumulationData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(accumulation: Accumulation): Boolean {
        return try {
            val accumulationData = accumulation.mapToAccumulationData()
            accumulationStorage.update(accumulationData = accumulationData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(accumulation: Accumulation): Boolean {
        return try {
            val accumulationData = accumulation.mapToAccumulationData()
            accumulationStorage.delete(accumulationData = accumulationData)
        }catch (e: Exception) {
            false
        }
    }
}