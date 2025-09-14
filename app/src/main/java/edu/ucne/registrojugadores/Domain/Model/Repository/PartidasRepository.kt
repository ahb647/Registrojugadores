package edu.ucne.registrojugadores.Domain.Model.Repository

import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import kotlinx.coroutines.flow.Flow

interface PartidasRepository {

    fun getAllPartidas(): Flow<List<Partida>>

    suspend fun getPartidaById(id: Int): Partida?

    suspend fun insertPartida(partida: Partida)

    suspend fun deletePartida(partida: Partida)

    suspend fun updatePartida(partida: Partida)
}