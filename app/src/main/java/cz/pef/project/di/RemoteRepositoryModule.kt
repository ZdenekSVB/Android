package cz.pef.project.di


import cz.pef.project.communication.GardenAPI
import cz.pef.project.communication.GardenRemoteRepositoryImpl
import cz.pef.project.communication.IGardenRemoteRepository
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
    fun providePetsRepository(gardenAPI: GardenAPI): IGardenRemoteRepository {
        return GardenRemoteRepositoryImpl(gardenAPI)
    }

}
