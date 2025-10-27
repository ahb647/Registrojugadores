package edu.ucne.registrojugadores.Data.remote


import edu.ucne.registrojugadores.Data.remote.dto.JugadorRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.JugadorResponseDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class JugadorRemoteDataSource @Inject constructor(
    private val api: GestionHuacalesAPI
) {
    suspend fun getJugadores(): Resource<List<JugadorResponseDto>> {
        return try {
            val response = api.getJugadores()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getJugador(id: Int): Resource<JugadorResponseDto> {
        return try {
            val response = api.getJugador(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postJugador(request: JugadorRequestDto): Resource<JugadorResponseDto> {
        return try {
            val response = api.postJugador(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putJugador(id: Int, request: JugadorRequestDto): Resource<Unit> {
        return try {
            val response = api.putJugador(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }
}