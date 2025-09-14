package edu.ucne.registrojugadores.ui.theme.screen.Partidas

import edu.ucne.registrojugadores.Domain.Model.Model.Partida

interface PartidasEvent {


    data class FechaChanged(val value: String) : PartidasEvent
    data class Jugador1Changed(val value: Int) : PartidasEvent
    data class Jugador2Changed(val value: Int) : PartidasEvent
    data class GanadorChanged(val value: Int) : PartidasEvent
    data class EsFinalizadaChanged(val value: Boolean) : PartidasEvent


    data class Save(val partida: Partida) : PartidasEvent
    data class Delete(val partida: Partida) : PartidasEvent
    data class Select(val partida: Partida) : PartidasEvent
}