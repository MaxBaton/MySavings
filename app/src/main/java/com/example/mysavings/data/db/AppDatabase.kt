package com.example.mysavings.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mysavings.data.db.dao.AccumulationDao
import com.example.mysavings.data.db.dao.ExpenditureDao
import com.example.mysavings.data.db.dao.ReplenishmentDao
import com.example.mysavings.data.db.dao.RestDao
import com.example.mysavings.data.models.db.AccumulationData
import com.example.mysavings.data.models.db.ExpenditureData
import com.example.mysavings.data.models.db.ReplenishmentData
import com.example.mysavings.data.models.db.RestData

@Database(
    entities = [RestData::class, ExpenditureData::class, AccumulationData::class, ReplenishmentData::class],
    version = 5,
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
                            .addMigrations(MIGRATION_4_5)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        private val MIGRATION_4_5 = object: Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "alter table ExpenditureData RENAME COLUMN text to description"
                )
            }

        }
    }
}