package edu.ucne.registrojugadores.Data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class JugadorRequestDto(
    val nombres: String,
    val email: String
)