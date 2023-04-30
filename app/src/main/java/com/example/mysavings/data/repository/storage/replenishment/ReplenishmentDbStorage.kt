package com.example.mysavings.data.repository.storage.replenishment

import com.example.mysavings.data.db.dao.ReplenishmentDao
import com.example.mysavings.data.models.ReplenishmentData

class ReplenishmentDbStorage(private val replenishmentDao: ReplenishmentDao): ReplenishmentStorage {
    override suspend fun getAllReplenishments(): MutableList<ReplenishmentData>? {
        return try {
            replenishmentDao.getAllReplenishments()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<ReplenishmentData>? {
        return try {
            replenishmentDao.getReplenishmentsForAccumulation(idAccumulation = idAccumulation)
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(replenishment: ReplenishmentData): Boolean {
        return try {
            replenishmentDao.add(replenishmentData = replenishment)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(replenishment: ReplenishmentData): Boolean {
        return try {
            replenishmentDao.update(replenishmentData = replenishment)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(replenishment: ReplenishmentData): Boolean {
        return try {
            replenishmentDao.delete(replenishmentData = replenishment)
            true
        }catch (e: Exception) {
            false
        }
    }
}