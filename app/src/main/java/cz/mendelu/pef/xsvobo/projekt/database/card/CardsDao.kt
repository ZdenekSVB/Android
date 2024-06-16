package cz.mendelu.pef.xsvobo.projekt.database.card

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.xsvobo.projekt.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {
    @Insert
    suspend fun insert(card: Card): Long

    @Update
    suspend fun update(card: Card)

    @Query("SELECT * FROM cards WHERE setsId = :id")
    fun getCardsBySetId(id: Long): Flow<List<Card>>

    @Delete
    suspend fun delete(card: Card): Int

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCard(id: Long): Card
}