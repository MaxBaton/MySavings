package com.example.mysavings.data.repository.storage.replenishment

import com.example.mysavings.data.models.db.ReplenishmentData

interface ReplenishmentStorage {
    suspend fun getAllReplenishments(): MutableList<ReplenishmentData>?

    suspend fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<ReplenishmentData>?

    suspend fun add(replenishment: ReplenishmentData): Boolean

    suspend fun update(replenishment: ReplenishmentData): Boolean

    suspend fun delete(replenishment: ReplenishmentData): Boolean
}