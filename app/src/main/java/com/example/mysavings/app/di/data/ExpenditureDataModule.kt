package com.example.mysavings.app.di.data

import com.example.mysavings.data.db.AppDatabase
import com.example.mysavings.data.db.dao.ExpenditureDao
import com.example.mysavings.data.repository.ExpenditureRepositoryImpl
import com.example.mysavings.data.repository.storage.expenditure.ExpenditureDbStorage
import com.example.mysavings.data.repository.storage.expenditure.ExpenditureStorage
import com.example.mysavings.domain.repository.ExpenditureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExpenditureDataModule {
    @Provides
    @Singleton
    fun provideExpenditureRepository(expenditureStorage: ExpenditureStorage): ExpenditureRepository {
        return ExpenditureRepositoryImpl(expenditureStorage = expenditureStorage)
    }

    @Provides
    @Singleton
    fun provideExpenditureDbStorage(expenditureDao: ExpenditureDao): ExpenditureStorage {
        return ExpenditureDbStorage(expenditureDao = expenditureDao)
    }

    @Provides
    @Singleton
    fun provideExpenditureDao(db: AppDatabase): ExpenditureDao {
        return db.expenditureDao()
    }
}