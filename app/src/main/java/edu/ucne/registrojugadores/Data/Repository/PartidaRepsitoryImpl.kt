package edu.ucne.registrojugadores.Data.Repository



import edu.ucne.registrojugadores.Data.Local.Dao.PartidaDao
import edu.ucne.registrojugadores.Data.mapper.asExternalModel
import edu.ucne.registrojugadores.Data.mapper.toEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.Domain.Model.Repository.PartidasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartidasRepositoryImpl @Inject constructor(
    private val dao: PartidaDao
) : PartidasRepository {

    override fun getAllPartidas(): Flow<List<Partida>> =
        dao.getAllPartidas().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getPartidaById(id: Int): Partida? =
        dao.getPartidaById(id)?.asExternalModel()

    override suspend fun insertPartida(partida: Partida) =
        dao.insertPartida(partida.toEntity())

    override suspend fun updatePartida(partida: Partida) =
        dao.updatePartida(partida.toEntity())

    override suspend fun deletePartida(partida: Partida) =
        dao.deletePartida(partida.toEntity())
}