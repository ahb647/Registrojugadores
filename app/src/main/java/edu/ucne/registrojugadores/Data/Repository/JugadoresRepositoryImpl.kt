package edu.ucne.registrojugadores.Data.Repository

import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.mapper.asExternalModel
import edu.ucne.registrojugadores.Data.mapper.toEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val dao: JugadorDao
) : JugadoresRepository {

    override fun getAllJugadores(): Flow<List<Jugadores>> {
        return dao.getAllJugadores().map { jugadores ->
            jugadores.map { it.asExternalModel() }
        }
    }

    override suspend fun getJugadorById(id: Int): Jugadores? {
        return dao.getJugadorById(id)?.asExternalModel()
    }

    override suspend fun insertJugador(jugador: Jugadores) {
        dao.insertJugador(jugador.toEntity())
    }

    override suspend fun deleteJugador(jugador: Jugadores) {
        dao.deleteJugador(jugador.toEntity())
    }

    override suspend fun updateJugador(jugador: Jugadores) {
        dao.updateJugador(jugador.toEntity())
    }
}