package cz.mendelu.pef.xsvobo.projekt.di

import cz.mendelu.pef.xsvobo.projekt.database.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.database.LocalSetsRepositoryImpl
import cz.mendelu.pef.xsvobo.projekt.database.SetsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalTasksRepository(dao: SetsDao): ILocalSetsRepository {
        return LocalSetsRepositoryImpl(dao)
    }

}