package cz.mendelu.pef.xsvobo.projekt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.xsvobo.projekt.model.Set

@Database(entities = [Set::class], version = 4, exportSchema = true)
abstract class SetsDatabase : RoomDatabase() {

    abstract fun setsDao(): SetsDao

    companion object {
        private var INSTANCE: SetsDatabase? = null

        fun getDatabase(context: Context): SetsDatabase {
            if (INSTANCE == null) {
                synchronized(SetsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SetsDatabase::class.java, "sets_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }

    }
}