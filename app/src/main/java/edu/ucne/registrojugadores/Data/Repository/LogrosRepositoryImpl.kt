package edu.ucne.registrojugadores.Data.Repository

import edu.ucne.registrojugadores.Data.Local.Dao.LogrosDao
import edu.ucne.registrojugadores.Data.mapper.asExternalModel
import edu.ucne.registrojugadores.Data.mapper.toEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Logros
import edu.ucne.registrojugadores.Domain.Model.Repository.LogrosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogrosRepositoryImpl @Inject constructor(
    private val dao: LogrosDao
) : LogrosRepository {


    override fun getAllLogros(): Flow<List<Logros>> =
        dao.observedAll().map { list -> list.map { it.asExternalModel() } }


    override suspend fun getLogroById(id: Int): Logros? =
        dao.getById(id)?.asExternalModel()


    override suspend fun insertLogro(logro: Logros) =
        dao.upsert(logro.toEntity())


    override suspend fun updateLogro(logro: Logros) =
        dao.upsert(logro.toEntity())


    override suspend fun deleteLogro(logro: Logros) =
        dao.delete(logro.toEntity())

    // Borra un logro por ID
    override suspend fun deleteLogroById(id: Int) =
        dao.deleteById(id)
}
