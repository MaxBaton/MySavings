package com.example.mysavings.data.db.dao

import androidx.room.*
import com.example.mysavings.data.models.db.RestData

@Dao
interface RestDao {
    @Query("select * from restData")
    fun getRest(): RestData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(restData: RestData)

    @Update
    fun update(restData: RestData)

    @Delete
    fun delete(restData: RestData)
}