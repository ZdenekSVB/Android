package cz.mendelu.pef.xsvobo.projekt.database.set

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.xsvobo.projekt.model.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface SetsDao {
    @Query("SELECT * FROM sets")
    fun getAll(): Flow<List<Set>>

    @Insert
    suspend fun insert(set: Set): Long

    @Update
    suspend fun update(set: Set)

    @Query("SELECT * FROM sets WHERE id = :id")
    suspend fun getSet(id: Long): Set

    @Query("SELECT * FROM sets ORDER BY latest DESC LIMIT 3")
    fun getLatestSet(): Flow<List<Set>>

    @Query("UPDATE sets SET icon = :iconURL WHERE id = :id")
    suspend fun updateSetIcon(id: Long, iconURL: String)

    @Query("SELECT COUNT(*) FROM sets WHERE id = :id")
    suspend fun getCardsCount(id: Long): Int

    @Delete
    suspend fun delete(set: Set): Int
}
