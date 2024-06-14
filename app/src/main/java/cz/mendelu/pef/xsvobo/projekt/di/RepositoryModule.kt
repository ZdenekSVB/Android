package cz.mendelu.pef.xsvobo.projekt.di

import cz.mendelu.pef.xsvobo.projekt.database.card.CardsDao
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.database.card.LocalCardsRepositoryImpl
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.database.set.LocalSetsRepositoryImpl
import cz.mendelu.pef.xsvobo.projekt.database.set.SetsDao
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
    fun provideLocalSetsRepository(dao: SetsDao): ILocalSetsRepository {
        return LocalSetsRepositoryImpl(dao)
    }
    @Provides
    @Singleton
    fun provideLocalCardsRepository(dao: CardsDao): ILocalCardsRepository {
        return LocalCardsRepositoryImpl(dao)
    }
}