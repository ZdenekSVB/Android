package cz.mendelu.pef.xsvobo.projekt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.util.readVersion
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.mendelu.pef.xsvobo.projekt.database.card.CardsDao
import cz.mendelu.pef.xsvobo.projekt.database.set.SetsDao
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.model.Set

@Database(entities = [Set::class, Card::class], version = 6, exportSchema = true)
abstract class SetsDatabase : RoomDatabase() {

    abstract fun setsDao(): SetsDao
    abstract fun cardsDao(): CardsDao

    companion object {
        private var INSTANCE: SetsDatabase? = null

        fun getDatabase(context: Context): SetsDatabase {
            if (INSTANCE == null) {
                synchronized(SetsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext, SetsDatabase::class.java, "sets_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun getDatabaseVersion(context: Context): Int {
            val db: SupportSQLiteDatabase = Room.databaseBuilder(
                context.applicationContext, SetsDatabase::class.java, "sets_database"
            ).build().openHelper.readableDatabase
            return db.version
        }
    }
}