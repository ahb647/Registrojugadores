package edu.ucne.registrojugadores.Data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class MovimientosRequestDto(
    val partidaId: Int,
    val jugador: String = "X",
    val posicionFila: Int,
    val posicionColumna: Int
)