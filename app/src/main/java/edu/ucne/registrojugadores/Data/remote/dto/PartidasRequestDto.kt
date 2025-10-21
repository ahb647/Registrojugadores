package edu.ucne.registrojugadores.Data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class PartidaRequestDto(
    val jugador1Id: Int,
    val jugador2Id: Int? = null
)