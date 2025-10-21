package edu.ucne.registrojugadores.Data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class JugadorResponseDto(
    val jugadorId: Int,
    val nombres: String,
    val email: String
)