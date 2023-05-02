package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.repository.Replenishment

interface ReplenishmentRepository {
    suspend fun getAllReplenishments(): MutableList<Replenishment>?

    suspend fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<Replenishment>?

    suspend fun add(replenishmentData: Replenishment): Boolean

    suspend fun update(replenishmentData: Replenishment): Boolean

    suspend fun delete(replenishmentData: Replenishment): Boolean
}