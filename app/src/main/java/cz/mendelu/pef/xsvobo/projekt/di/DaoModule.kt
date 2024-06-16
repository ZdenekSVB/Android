package cz.mendelu.pef.xsvobo.projekt.di

import cz.mendelu.pef.xsvobo.projekt.database.SetsDatabase
import cz.mendelu.pef.xsvobo.projekt.database.card.CardsDao
import cz.mendelu.pef.xsvobo.projekt.database.set.SetsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideSetDao(database: SetsDatabase): SetsDao {
        return database.setsDao()
    }

    @Provides
    @Singleton
    fun provideCardDao(database: SetsDatabase): CardsDao {
        return database.cardsDao()
    }
}