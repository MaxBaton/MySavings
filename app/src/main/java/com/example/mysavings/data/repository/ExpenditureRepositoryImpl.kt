package com.example.mysavings.data.repository

import com.example.mysavings.data.mappers.mapToExpenditureData
import com.example.mysavings.data.mappers.mapToListExpenditure
import com.example.mysavings.data.repository.storage.expenditure.ExpenditureStorage
import com.example.mysavings.domain.models.repository.Expenditure
import com.example.mysavings.domain.repository.ExpenditureRepository

class ExpenditureRepositoryImpl(private val expenditureStorage: ExpenditureStorage): ExpenditureRepository {
    override suspend fun getExpenses(): MutableList<Expenditure>? {
        return try {
            expenditureStorage.getExpenses()?.mapToListExpenditure()?.toMutableList()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(expenditure: Expenditure): Boolean {
        return try {
            val expenditureData = expenditure.mapToExpenditureData()
            expenditureStorage.add(expenditureData = expenditureData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(expenditure: Expenditure): Boolean {
        return try {
            val expenditureData = expenditure.mapToExpenditureData()
            expenditureStorage.update(expenditureData = expenditureData)
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(expenditure: Expenditure): Boolean {
        return try {
            val expenditureData = expenditure.mapToExpenditureData()
            expenditureStorage.delete(expenditureData = expenditureData)
        }catch (e: Exception) {
            false
        }
    }
}