package com.example.mysavings.app.di.data

import com.example.mysavings.data.db.AppDatabase
import com.example.mysavings.data.db.dao.ReplenishmentDao
import com.example.mysavings.data.repository.ReplenishmentRepositoryImpl
import com.example.mysavings.data.repository.storage.replenishment.ReplenishmentDbStorage
import com.example.mysavings.data.repository.storage.replenishment.ReplenishmentStorage
import com.example.mysavings.domain.repository.ReplenishmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReplenishmentDataModule {
    @Provides
    @Singleton
    fun provideReplenishmentRepository(replenishmentStorage: ReplenishmentStorage): ReplenishmentRepository {
        return ReplenishmentRepositoryImpl(replenishmentStorage = replenishmentStorage)
    }

    @Provides
    @Singleton
    fun provideReplenishmentDbStorage(replenishmentDao: ReplenishmentDao): ReplenishmentStorage {
        return ReplenishmentDbStorage(replenishmentDao = replenishmentDao)
    }

    @Provides
    @Singleton
    fun provideReplenishmentDao(db: AppDatabase): ReplenishmentDao {
        return db.replenishmentDao()
    }
}