package com.example.mysavings.data.repository.storage.rest

import com.example.mysavings.data.models.RestData

interface RestStorage {
    suspend fun getRest(): Float

    suspend fun add(restData: RestData): Boolean

    suspend fun update(restData: RestData): Boolean

    suspend fun delete(restData: RestData): Boolean
}