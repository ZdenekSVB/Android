package cz.pef.project.di


import cz.pef.project.communication.IPetsRemoteRepository
import cz.pef.project.communication.PetsAPI
import cz.pef.project.communication.PetsRemoteRepositoryImpl
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
