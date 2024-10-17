package com.example.mystery.di


import com.example.mystery.communication.IMysteryRemoteRepository
import com.example.mystery.communication.MysteryAPI
import com.example.mystery.communication.MysteryRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun providePetsRepository(mysteryAPI: MysteryAPI): IMysteryRemoteRepository {
        return MysteryRemoteRepositoryImpl(mysteryAPI)
    }
}