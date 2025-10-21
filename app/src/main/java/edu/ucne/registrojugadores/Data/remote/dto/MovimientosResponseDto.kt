package edu.ucne.registrojugadores.Data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovimientosResponseDto(
    val jugador: String = "X",
    val posicionFila: Int,
    val posicionColumna: Int
)