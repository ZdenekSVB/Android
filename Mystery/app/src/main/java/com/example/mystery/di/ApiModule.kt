package com.example.mystery.di

import com.example.mystery.communication.MysteryAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
@Provides
@Singleton
fun providePetsAPI(retrofit: Retrofit): MysteryAPI {
    return retrofit.create(MysteryAPI::class.java)
}

}