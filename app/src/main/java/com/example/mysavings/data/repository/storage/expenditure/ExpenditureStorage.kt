package com.example.mysavings.data.repository.storage.expenditure

import com.example.mysavings.data.models.db.ExpenditureData

interface ExpenditureStorage {
    suspend fun getExpenses(): MutableList<ExpenditureData>?

    suspend fun add(expenditureData: ExpenditureData): Boolean

    suspend fun update(expenditureData: ExpenditureData): Boolean

    suspend fun delete(expenditureData: ExpenditureData): Boolean
}