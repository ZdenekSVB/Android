package cz.pef.project.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.pef.project.DB.PlantEntity
import cz.pef.project.DB.UserEntity

@Database(entities = [UserEntity::class, PlantEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
