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

    @Query(
        "select id from expendituredata " +
                "where date = :date " +
                "      and sum = :sum " +
                "      and description = :description"
    )
    fun getIdByData(date: String, sum: Float, description: String ): Int
}