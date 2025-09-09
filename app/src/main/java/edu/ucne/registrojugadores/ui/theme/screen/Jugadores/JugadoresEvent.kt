package edu.ucne.registrojugadores.ui.theme.screen.Jugadores

import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores

interface JugadoresEvent{


data class NombreChanged(val value: String) : JugadoresEvent
data class PartidasChanged(val value: Int) : JugadoresEvent


data class Save(val jugador: Jugadores) : JugadoresEvent
data class Delete(val jugador: Jugadores) : JugadoresEvent
data class Select(val jugador: Jugadores) : JugadoresEvent


}