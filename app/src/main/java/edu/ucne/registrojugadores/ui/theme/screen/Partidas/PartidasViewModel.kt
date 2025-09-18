package edu.ucne.registrojugadores.ui.theme.screen.Partidas

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.Domain.Model.Repository.PartidasRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartidasViewModel @Inject constructor(
    private val repository: PartidasRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PartidasState())
    val state: StateFlow<PartidasState> = _state.asStateFlow()

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    fun onEvent(event: PartidasEvent) {
        when (event) {
            is PartidasEvent.FechaChanged -> {
                _state.value = _state.value.copy(fecha = event.value)
            }
            is PartidasEvent.Jugador1Changed -> {
                _state.value = _state.value.copy(jugador1Id = event.value)
            }
            is PartidasEvent.Jugador2Changed -> {
                _state.value = _state.value.copy(jugador2Id = event.value)
            }
            is PartidasEvent.GanadorChanged -> {
                val finalizado = true // Siempre finalizada, empate incluido
                _state.value = _state.value.copy(
                    ganadorId = event.value,
                    esFinalizada = finalizado
                )
            }
            is PartidasEvent.Save -> guardarPartida(event.partida)
            is PartidasEvent.Delete -> eliminarPartida(event.partida)
            is PartidasEvent.Select -> {
                _state.value = _state.value.copy(
                    partidaId = event.partida.partidaId,
                    fecha = event.partida.fecha,
                    jugador1Id = event.partida.jugador1Id ?: 1, // valor por defecto si es null
                    jugador2Id = event.partida.jugador2Id ?: 2, // valor por defecto si es null
                    ganadorId = event.partida.ganadorId,
                    esFinalizada = event.partida.esFinalizada
                )
            }
        }
    }

    private fun guardarPartida(partida: Partida) {
        viewModelScope.launch {
            // Marcar siempre como finalizada, empate incluido
            val partidaActualizada = partida.copy(esFinalizada = true)
            repository.insertPartida(partidaActualizada)
            sendEvent("Partida guardada correctamente")
        }
    }

    private fun eliminarPartida(partida: Partida) {
        viewModelScope.launch {
            repository.deletePartida(partida)
            sendEvent("Partida eliminada")
        }
    }

    // Función auxiliar para insertar desde UI (opcional)
    fun insertarPartida(partida: Partida) {
        viewModelScope.launch {
            guardarPartida(partida)
        }
    }

    fun eliminarPartidaById(partidaId: Int) {
        val partida = _state.value.partidas.firstOrNull { it.partidaId == partidaId }
        partida?.let { eliminarPartida(it) }
    }
}
