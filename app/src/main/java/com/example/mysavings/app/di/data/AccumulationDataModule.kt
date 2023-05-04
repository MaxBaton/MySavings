package com.example.mysavings.app.di.data

import com.example.mysavings.data.db.AppDatabase
import com.example.mysavings.data.db.dao.AccumulationDao
import com.example.mysavings.data.repository.AccumulationRepositoryImpl
import com.example.mysavings.data.repository.storage.accumulation.AccumulationDbStorage
import com.example.mysavings.data.repository.storage.accumulation.AccumulationStorage
import com.example.mysavings.domain.repository.AccumulationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccumulationDataModule {
    @Provides
    @Singleton
    fun provideAccumulationRepository(accumulationStorage: AccumulationStorage): AccumulationRepository {
        return AccumulationRepositoryImpl(accumulationStorage = accumulationStorage)
    }

    @Provides
    @Singleton
    fun provideAccumulationDbStorage(accumulationDao: AccumulationDao): AccumulationStorage {
        return AccumulationDbStorage(accumulationDao = accumulationDao)
    }

    @Provides
    @Singleton
    fun provideAccumulationDao(db: AppDatabase): AccumulationDao {
        return db.accumulationDao()
    }
}