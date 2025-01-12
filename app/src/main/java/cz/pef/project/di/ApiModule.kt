package cz.pef.project.di

import cz.pef.project.communication.GardenAPI
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
    fun provideGardenAPI(retrofit: Retrofit): GardenAPI {
        return retrofit.create(GardenAPI::class.java)
    }

}