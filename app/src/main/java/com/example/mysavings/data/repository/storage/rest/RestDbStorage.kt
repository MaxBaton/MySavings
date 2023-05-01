package com.example.mysavings.data.repository.storage.rest

import com.example.mysavings.data.db.dao.RestDao
import com.example.mysavings.data.models.db.RestData

class RestDbStorage(private val restDao: RestDao): RestStorage {
    override suspend fun getRest(): RestData? {
        return try {
            restDao.getRest()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(restData: RestData): Boolean {
        return try {
            restDao.add(restData = restData)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(restData: RestData): Boolean {
        return try {
            restDao.update(restData = restData)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(restData: RestData): Boolean {
        return try {
            restDao.delete(restData = restData)
            true
        }catch (e: Exception) {
            false
        }
    }
}