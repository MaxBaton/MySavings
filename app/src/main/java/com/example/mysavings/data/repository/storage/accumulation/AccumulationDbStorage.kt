package com.example.mysavings.data.repository.storage.accumulation

import com.example.mysavings.data.db.dao.AccumulationDao
import com.example.mysavings.data.models.db.AccumulationData

class AccumulationDbStorage(private val accumulationDao: AccumulationDao): AccumulationStorage {
    override suspend fun getAccumulation(): MutableList<AccumulationData>? {
        return try {
            accumulationDao.getAccumulation()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(accumulationData: AccumulationData): Int {
        return try {
            accumulationDao.add(accumulationData = accumulationData)
            val id = accumulationDao.getIdByData(
                name = accumulationData.name,
                sum = accumulationData.sum
            )
            id
        }catch (e: Exception) {
            -1
        }
    }

    override suspend fun update(accumulationData: AccumulationData): Boolean {
        return try {
            accumulationDao.update(accumulationData = accumulationData)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(accumulationData: AccumulationData): Boolean {
        return try {
            accumulationDao.delete(accumulationData = accumulationData)
            true
        }catch (e: Exception) {
            false
        }
    }
}