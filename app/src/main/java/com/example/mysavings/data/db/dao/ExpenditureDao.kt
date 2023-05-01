package com.example.mysavings.data.db.dao

import androidx.room.*
import com.example.mysavings.data.models.db.ExpenditureData

@Dao
interface ExpenditureDao {
    @Query("select * from expenditureData")
    fun getExpenses(): MutableList<ExpenditureData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(expenditureData: ExpenditureData)

    @Update
    fun update(expenditureData: ExpenditureData)

    @Delete
    fun delete(expenditureData: ExpenditureData)
}