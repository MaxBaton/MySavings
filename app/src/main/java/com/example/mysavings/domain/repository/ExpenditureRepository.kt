package com.example.mysavings.domain.repository

import com.example.mysavings.domain.models.repository.Expenditure

interface ExpenditureRepository {
    suspend fun getExpenses(): MutableList<Expenditure>?

    suspend fun add(expenditure: Expenditure): Boolean

    suspend fun update(expenditure: Expenditure): Boolean

    suspend fun delete(expenditure: Expenditure): Boolean
}