package edu.ucne.registrojugadores.Domain.Model.Repository

import edu.ucne.registrojugadores.Domain.Model.Model.Logros
import kotlinx.coroutines.flow.Flow

interface LogrosRepository {


    fun getAllLogros(): Flow<List<Logros>>


    suspend fun getLogroById(id: Int): Logros?


    suspend fun insertLogro(logro: Logros)


    suspend fun updateLogro(logro: Logros)


    suspend fun deleteLogro(logro: Logros)


    suspend fun deleteLogroById(id: Int)
}