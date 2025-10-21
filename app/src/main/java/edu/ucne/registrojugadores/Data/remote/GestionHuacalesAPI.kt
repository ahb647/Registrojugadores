package edu.ucne.registrojugadores.Data.remote

import edu.ucne.registrojugadores.Data.remote.dto.JugadorRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.JugadorResponseDto
import edu.ucne.registrojugadores.Data.remote.dto.MovimientosRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.MovimientosResponseDto
import edu.ucne.registrojugadores.Data.remote.dto.PartidaRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.PartidaResponseDto
import retrofit2.Response
import retrofit2.http.*

interface GestionHuacalesAPI {

    @GET("api/Jugadores")
    suspend fun getJugadores(): Response<List<JugadorResponseDto>>

    @GET("api/Jugadores/{id}")
    suspend fun getJugador(@Path("id") id: Int): Response<JugadorResponseDto>

    @POST("api/Jugadores")
    suspend fun postJugador(@Body jugador: JugadorRequestDto): Response<JugadorResponseDto>

    @PUT("api/Jugadores/{id}")
    suspend fun putJugador(@Path("id") id: Int, @Body jugador: JugadorRequestDto): Response<Unit>


    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): Response<List<MovimientosResponseDto>>

    @POST("api/Movimientos")
    suspend fun postMovimiento(@Body movimiento: MovimientosRequestDto): Response<Unit>

    @DELETE("api/Movimientos/{id}")
    suspend fun deleteMovimiento(@Path("id") id: Int): Response<Unit>


    @GET("api/Partidas")
    suspend fun getPartidas(): Response<List<PartidaResponseDto>>

    @GET("api/Partidas/{id}")
    suspend fun getPartida(@Path("id") id: Int): Response<PartidaResponseDto>

    @POST("api/Partidas")
    suspend fun postPartida(@Body partida: PartidaRequestDto): Response<PartidaResponseDto>

    @PUT("api/Partidas/{id}")
    suspend fun putPartida(@Path("id") id: Int, @Body partida: PartidaRequestDto): Response<Unit>
}
