package cz.pef.mendelu.examtemplate2024.di

import cz.pef.mendelu.examtemplate2024.communication.IUserRemoteRepository
import cz.pef.mendelu.examtemplate2024.communication.UserAPI
import cz.pef.mendelu.examtemplate2024.communication.UserRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRemoteRepository(userAPI: UserAPI): IUserRemoteRepository {
        return UserRemoteRepositoryImpl(userAPI)
    }
}
