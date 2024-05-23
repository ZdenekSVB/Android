package cz.mendelu.pef.xsvobo.projekt.di

import android.content.Context
import cz.mendelu.pef.xsvobo.projekt.database.SetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SetsDatabase {
        return SetsDatabase.getDatabase(context)
    }
}