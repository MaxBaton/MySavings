package com.example.mysavings.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mysavings.data.db.dao.AccumulationDao
import com.example.mysavings.data.db.dao.ExpenditureDao
import com.example.mysavings.data.db.dao.ReplenishmentDao
import com.example.mysavings.data.db.dao.RestDao
import com.example.mysavings.data.models.AccumulationData
import com.example.mysavings.data.models.ExpenditureData
import com.example.mysavings.data.models.ReplenishmentData
import com.example.mysavings.data.models.RestData

@Database(
    entities = [RestData::class, ExpenditureData::class, AccumulationData::class, ReplenishmentData::class],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun restDao(): RestDao

    abstract fun expenditureDao(): ExpenditureDao

    abstract fun accumulationDao(): AccumulationDao

    abstract fun replenishmentDao(): ReplenishmentDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "mySavingsDatabase"

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            DB_NAME
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}