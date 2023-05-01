package com.example.mysavings.data.repository

import com.example.mysavings.data.mappers.mapToRest
import com.example.mysavings.data.mappers.mapToRestData
import com.example.mysavings.data.repository.storage.rest.RestStorage
import com.example.mysavings.domain.models.Rest
import com.example.mysavings.domain.repository.RestRepository

class RestRepositoryImpl(private val restStorage: RestStorage): RestRepository {
    override suspend fun getRest(): Rest? {
        return try {
            restStorage.getRest()?.mapToRest()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(rest: Rest): Boolean {
        return try {
            val restData = rest.mapToRestData()
            restStorage.add(restData = restData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(rest: Rest): Boolean {
        return try {
            val restData = rest.mapToRestData()
            restStorage.update(restData = restData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(rest: Rest): Boolean {
        return try {
            val restData = rest.mapToRestData()
            restStorage.delete(restData = restData)
        }catch (e: Exception) {
            false
        }
    }
}