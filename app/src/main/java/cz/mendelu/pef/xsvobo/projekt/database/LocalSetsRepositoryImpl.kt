package cz.mendelu.pef.xsvobo.projekt.database

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

    override suspend fun delete(set: Set): Int {
        return dao.delete(set)
    }
}