package com.example.mysavings.data.repository

import com.example.mysavings.data.mappers.mapToListReplenishment
import com.example.mysavings.data.mappers.mapToReplenishmentData
import com.example.mysavings.data.repository.storage.replenishment.ReplenishmentStorage
import com.example.mysavings.domain.models.Replenishment
import com.example.mysavings.domain.repository.ReplenishmentRepository

class ReplenishmentRepositoryImpl(private val replenishmentStorage: ReplenishmentStorage): ReplenishmentRepository {
    override suspend fun getAllReplenishments(): MutableList<Replenishment>? {
        return try {
            replenishmentStorage.getAllReplenishments()?.mapToListReplenishment()?.toMutableList()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<Replenishment>? {
        return try {
            replenishmentStorage.getReplenishmentsForAccumulation(idAccumulation = idAccumulation)?.mapToListReplenishment()?.toMutableList()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(replenishmentData: Replenishment): Boolean {
        return try {
            val replenishment = replenishmentData.mapToReplenishmentData()
            replenishmentStorage.add(replenishment = replenishment)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(replenishmentData: Replenishment): Boolean {
        return try {
            val replenishment = replenishmentData.mapToReplenishmentData()
            replenishmentStorage.update(replenishment = replenishment)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(replenishmentData: Replenishment): Boolean {
        return try {
            val replenishment = replenishmentData.mapToReplenishmentData()
            replenishmentStorage.delete(replenishment = replenishment)
        }catch (e: Exception) {
            false
        }
    }
}