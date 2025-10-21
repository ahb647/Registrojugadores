package edu.ucne.registrojugadores.Data.Repository


import edu.ucne.registrojugadores.Data.remote.dto.JugadorRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.MovimientosRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.PartidaRequestDto
import edu.ucne.registrojugadores.Data.remote.GestionHuacalesAPI
import javax.inject.Inject

class GestionHuacalesApiRepository @Inject constructor(
    private val api: GestionHuacalesAPI
) {
    private suspend fun <T> handleResponse(call: suspend () -> retrofit2.Response<T>): Result<T?> {
        return try {
            val response = call()
            when (response.code()) {
                in 200..299 -> Result.success(response.body())
                400 -> Result.failure(Exception("Error 400: Solicitud incorrecta."))
                401 -> Result.failure(Exception("Error 401: No autorizado."))
                403 -> Result.failure(Exception("Error 403: Acceso prohibido."))
                404 -> Result.failure(Exception("Error 404: Recurso no encontrado."))
                500 -> Result.failure(Exception("Error 500: Error interno del servidor."))
                503 -> Result.failure(Exception("Error 503: Servicio no disponible."))
                else -> Result.failure(Exception("Error desconocido: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJugadores() = handleResponse { api.getJugadores() }
    suspend fun getJugador(id: Int) = handleResponse { api.getJugador(id) }
    suspend fun postJugador(jugador: JugadorRequestDto) = handleResponse { api.postJugador(jugador) }
    suspend fun putJugador(id: Int, jugador: JugadorRequestDto) = handleResponse { api.putJugador(id, jugador) }

    suspend fun getMovimientos(partidaId: Int) = handleResponse { api.getMovimientos(partidaId) }
    suspend fun postMovimiento(movimiento: MovimientosRequestDto) = handleResponse { api.postMovimiento(movimiento) }
    suspend fun deleteMovimiento(id: Int) = handleResponse { api.deleteMovimiento(id) }

    suspend fun getPartidas() = handleResponse { api.getPartidas() }
    suspend fun getPartida(id: Int) = handleResponse { api.getPartida(id) }
    suspend fun postPartida(partida: PartidaRequestDto) = handleResponse { api.postPartida(partida) }
    suspend fun putPartida(id: Int, partida: PartidaRequestDto) = handleResponse { api.putPartida(id, partida) }
}