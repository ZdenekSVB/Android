package cz.mendelu.pef.xsvobo.projekt.database.card

import cz.mendelu.pef.xsvobo.projekt.model.Card
import kotlinx.coroutines.flow.Flow

interface ILocalCardsRepository {
    suspend fun insert(card: Card): Long
    suspend fun update(card: Card)
    suspend fun getCardsBySetId(id: Long): Flow<List<Card>>
    suspend fun delete(card: Card): Int
    suspend fun getCard(id: Long): Card
}