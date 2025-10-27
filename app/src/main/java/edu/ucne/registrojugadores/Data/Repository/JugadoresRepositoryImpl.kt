package edu.ucne.registrojugadores.Data.Repository

import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.mapper.asExternalModel
import edu.ucne.registrojugadores.Data.mapper.toEntity
import edu.ucne.registrojugadores.Data.remote.JugadorRemoteDataSource
import edu.ucne.registrojugadores.Data.remote.Resource
import edu.ucne.registrojugadores.Data.remote.dto.JugadorRequestDto
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JugadorRepositoryImpl @Inject constructor(
    private val dao: JugadorDao,
    private val remoteDataSource: JugadorRemoteDataSource
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
        val request = JugadorRequestDto(
            nombres = jugador.nombres,
            email = jugador.email
        )
        when (val result = remoteDataSource.postJugador(request)) {
            is Resource.Success -> {
                result.data?.let { dto ->
                    dao.insertJugador(dto.toEntity())
                }
            }
            is Resource.Error -> {
                dao.insertJugador(jugador.toEntity())
            }
            else -> {}
        }
    }

    override suspend fun updateJugador(jugador: Jugadores) {
        val request = JugadorRequestDto(
            nombres = jugador.nombres,
            email = jugador.email
        )
        when (val result = remoteDataSource.putJugador(jugador.jugadorId, request)) {
            is Resource.Success -> {
                dao.updateJugador(jugador.toEntity())
            }
            is Resource.Error -> {
                dao.updateJugador(jugador.toEntity())
            }
            else -> {}
        }
    }

    override suspend fun deleteJugador(jugador: Jugadores) {
        dao.deleteJugador(jugador.toEntity())
    }

    override suspend fun syncJugadores(): Resource<Unit> {
        return when (val result = remoteDataSource.getJugadores()) {
            is Resource.Success -> {
                result.data?.let { lista ->
                    dao.clearAll()
                    dao.insertJugadores(lista.map { it.toEntity() })
                }
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error")
            else -> Resource.Loading()
        }
    }
}