package com.example.mysavings.data.db.dao

import androidx.room.*
import com.example.mysavings.data.models.db.AccumulationData

@Dao
interface AccumulationDao {
    @Query("select * from accumulationData")
    fun getAccumulation(): MutableList<AccumulationData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(accumulationData: AccumulationData)

    @Update
    fun update(accumulationData: AccumulationData)

    @Delete
    fun delete(accumulationData: AccumulationData)
}