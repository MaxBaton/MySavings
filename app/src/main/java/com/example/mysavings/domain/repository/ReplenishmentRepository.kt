package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.repository.Replenishment

interface ReplenishmentRepository {
    suspend fun getAllReplenishments(): MutableList<Replenishment>?

    suspend fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<Replenishment>?

    suspend fun add(replenishment: Replenishment): Boolean

    suspend fun update(replenishment: Replenishment): Boolean

    suspend fun delete(replenishmentData: Replenishment): Boolean
}