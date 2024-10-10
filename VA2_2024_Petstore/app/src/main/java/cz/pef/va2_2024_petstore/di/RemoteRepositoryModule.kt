package cz.pef.va2_2024_petstore.di


import cz.pef.va2_2024_petstore.communication.IPetsRemoteRepository
import cz.pef.va2_2024_petstore.communication.PetsAPI
import cz.pef.va2_2024_petstore.communication.PetsRemoteRepositoryImpl
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
    fun providePetsRepository(petsAPI: PetsAPI): IPetsRemoteRepository {
        return PetsRemoteRepositoryImpl(petsAPI)
    }
}