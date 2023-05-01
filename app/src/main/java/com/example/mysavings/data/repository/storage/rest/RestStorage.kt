package com.example.mysavings.data.repository.storage.rest

import com.example.mysavings.data.models.db.RestData

interface RestStorage {
    suspend fun getRest(): RestData?

    suspend fun add(restData: RestData): Boolean

    suspend fun update(restData: RestData): Boolean

    suspend fun delete(restData: RestData): Boolean
}