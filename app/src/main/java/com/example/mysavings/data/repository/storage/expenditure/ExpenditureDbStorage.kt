package com.example.mysavings.data.repository.storage.expenditure

import com.example.mysavings.data.db.dao.ExpenditureDao
import com.example.mysavings.data.models.ExpenditureData

class ExpenditureDbStorage(private val expenditureDao: ExpenditureDao): ExpenditureStorage {
    override suspend fun getExpenses(): MutableList<ExpenditureData>? {
        return try {
            expenditureDao.getExpenses()
        }catch (e: Exception) {
            null
        }
    }

    override suspend fun add(expenditureData: ExpenditureData): Boolean {
        return try {
            expenditureDao.add(expenditureData = expenditureData)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun update(expenditureData: ExpenditureData): Boolean {
        return try {
            expenditureDao.update(expenditureData = expenditureData)
            true
        }catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(expenditureData: ExpenditureData): Boolean {
        return try {
            expenditureDao.delete(expenditureData = expenditureData)
            true
        }catch (e: Exception) {
            false
        }
    }
}