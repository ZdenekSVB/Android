package cz.mendelu.pef.xsvobo.projekt.database.set

import android.util.Log
import cz.mendelu.pef.xsvobo.projekt.model.Set
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSetsRepositoryImpl @Inject constructor(private val dao: SetsDao) : ILocalSetsRepository {

    override fun getAll(): Flow<List<Set>> {
        return dao.getAll()
    }

    override suspend fun insert(set: Set): Long {
        return dao.insert(set)
    }

    override suspend fun update(set: Set) {
        dao.update(set)
    }

    override suspend fun getSet(id: Long): Set {
        return dao.getSet(id)
    }

    override suspend fun updateSetIcon(setId: Long, iconPath: String) {
        val set = dao.getSet(setId)
        if (set != null) {
            set.icon = iconPath
            dao.update(set)
        } else {
            Log.e("LocalSetsRepositoryImpl", "Set with id $setId not found")
            // Handle the case where the Set is null, maybe throw an exception or log an error
        }
    }


    override suspend fun getCardsCount(id: Long): Int {
        return dao.getCardsCount(id)
    }

    override  fun getLatestSet(): Flow<List<Set>> {
        return dao.getLatestSet()
    }

    override suspend fun delete(set: Set): Int {
        return dao.delete(set)
    }
}
