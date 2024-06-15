package cz.mendelu.pef.xsvobo.projekt.di

import android.content.Context
import cz.mendelu.pef.xsvobo.projekt.datastore.SetPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context) : SetPreferences{
        return SetPreferences(context)
    }

}