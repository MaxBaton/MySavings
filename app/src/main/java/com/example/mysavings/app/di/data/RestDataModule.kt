package com.example.mysavings.app.di.data

import com.example.mysavings.data.db.AppDatabase
import com.example.mysavings.data.db.dao.RestDao
import com.example.mysavings.data.repository.RestRepositoryImpl
import com.example.mysavings.data.repository.storage.rest.RestDbStorage
import com.example.mysavings.data.repository.storage.rest.RestStorage
import com.example.mysavings.domain.repository.RestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RestDataModule {
    @Provides
    @Singleton
    fun provideRestRepository(restStorage: RestStorage): RestRepository {
        return RestRepositoryImpl(restStorage = restStorage)
    }

    @Provides
    @Singleton
    fun provideRestDbStorage(restDao: RestDao): RestStorage {
        return RestDbStorage(restDao = restDao)
    }

    @Provides
    @Singleton
    fun provideRestDao(db: AppDatabase): RestDao {
        return db.restDao()
    }
}