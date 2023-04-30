package com.example.mysavings.data.db.dao

import androidx.room.*
import com.example.mysavings.data.models.ReplenishmentData

@Dao
interface ReplenishmentDao {
    @Query("select * from replenishmentData")
    fun getAllReplenishments(): MutableList<ReplenishmentData>

    @Query("select * from replenishmentData where idAccumulation = :idAccumulation")
    fun getReplenishmentsForAccumulation(idAccumulation: Int): MutableList<ReplenishmentData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(replenishmentData: ReplenishmentData)

    @Update
    fun update(replenishmentData: ReplenishmentData)

    @Delete
    fun delete(replenishmentData: ReplenishmentData)
}