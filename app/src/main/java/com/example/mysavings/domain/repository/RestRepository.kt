package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.repository.Rest

interface RestRepository {
    suspend fun getRest(): Rest?

    suspend fun add(rest: Rest): Boolean

    suspend fun update(rest: Rest): Boolean

    suspend fun delete(rest: Rest): Boolean
}