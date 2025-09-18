package edu.ucne.registrojugadores.Domain.Model.Repository

import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import kotlinx.coroutines.flow.Flow

interface JugadoresRepository {

    fun getAllJugadores(): Flow<List<Jugadores>>

    suspend fun getJugadorById(id: Int): Jugadores?

    suspend fun insertJugador(jugador: Jugadores)

    suspend fun deleteJugador(jugador: Jugadores)

    suspend fun updateJugador(jugador: Jugadores)

    suspend fun incrementarPartidas(jugadorId: Int)
}