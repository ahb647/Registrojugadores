package edu.ucne.registrojugadores.ui.theme.screen.Partidas


import edu.ucne.registrojugadores.Domain.Model.Model.Partida

data class PartidasState(
    val partidaId: Int? = null,
    val fecha: String = "",
    val jugador1Id: Int = 1,
    val jugador2Id: Int = 2,
    val ganadorId: Int = 0,
    val esFinalizada: Boolean = false,
    val partidas: List<Partida> = emptyList()
)