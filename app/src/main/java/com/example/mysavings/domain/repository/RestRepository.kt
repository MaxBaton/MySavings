package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.Rest

interface RestRepository {
    suspend fun getRest(): Float

    suspend fun add(rest: Rest): Boolean

    suspend fun update(rest: Rest): Boolean

    suspend fun delete(rest: Rest): Boolean
}