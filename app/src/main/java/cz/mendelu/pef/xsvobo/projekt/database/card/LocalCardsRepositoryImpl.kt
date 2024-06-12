package cz.mendelu.pef.xsvobo.projekt.database.card

import cz.mendelu.pef.xsvobo.projekt.model.Card
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalCardsRepositoryImpl @Inject constructor(private val dao: CardsDao) :
    ILocalCardsRepository {

    override suspend fun insert(card: Card): Long {
        return dao.insert(card)
    }

    override suspend fun update(card: Card) {
        dao.update(card)
    }

    override suspend fun getCardsBySetId(id: Long): Flow<List<Card>> {
        return dao.getCardsBySetId(id)
    }

    override suspend fun delete(card: Card): Int {
        return dao.delete(card)
    }

    override suspend fun getCard(id: Long): Card {
        return dao.getCard(id)
    }
}