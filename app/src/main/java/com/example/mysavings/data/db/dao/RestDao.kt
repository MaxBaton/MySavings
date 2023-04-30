package com.example.mysavings.data.db.dao

import androidx.room.*
import com.example.mysavings.data.models.RestData

@Dao
interface RestDao {
    @Query("select rest from restData")
    fun getRest(): Float

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(restData: RestData)

    @Update
    fun update(restData: RestData)

    @Delete
    fun delete(restData: RestData)
}