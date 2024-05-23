package cz.mendelu.pef.xsvobo.projekt.database

import cz.mendelu.pef.xsvobo.projekt.model.Set
import kotlinx.coroutines.flow.Flow

interface ILocalSetsRepository {
    fun getAll(): Flow<List<Set>>
    suspend fun insert(set: Set): Long

    suspend fun update(set: Set)
    suspend fun getSet(id: Long): Set
    suspend fun delete(set: Set): Int


}