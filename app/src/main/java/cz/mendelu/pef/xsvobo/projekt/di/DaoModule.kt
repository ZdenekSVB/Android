package cz.mendelu.pef.xsvobo.projekt.di
import cz.mendelu.pef.xsvobo.projekt.database.SetsDao
import cz.mendelu.pef.xsvobo.projekt.database.SetsDatabase
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
    fun provideTasksDao(database: SetsDatabase): SetsDao {
        return database.setsDao()
    }
}