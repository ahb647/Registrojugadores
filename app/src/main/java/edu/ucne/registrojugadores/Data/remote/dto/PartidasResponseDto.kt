package edu.ucne.registrojugadores.Data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class PartidaResponseDto(
    val partidaId: Int,
    val jugador1Id: Int,
    val jugador2Id: Int? = null
)
