package edu.ucne.registrojugadores.ui.theme.screen.Jugadores

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadoresViewModel @Inject constructor(
    private val repository: JugadoresRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(JugadoresState())
    val state: StateFlow<JugadoresState> = _state.asStateFlow()

    // Propiedad para exponer la lista de jugadores como StateFlow
    val jugadores: StateFlow<List<Jugadores>> = repository.getAllJugadores().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Canal de eventos para mensajes simples (Snackbar)
    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    fun onEvent(event: JugadoresEvent) {
        when (event) {
            is JugadoresEvent.NombreChanged -> {
                _state.value = _state.value.copy(nombres = event.value)
            }
            is JugadoresEvent.PartidasChanged -> {
                _state.value = _state.value.copy(partidas = event.value)
            }
            is JugadoresEvent.Save -> {
                insertarJugador(event.jugador)
            }
            is JugadoresEvent.Delete -> {
                eliminarJugador(event.jugador)
            }
            is JugadoresEvent.Select -> {
                _state.value = _state.value.copy(
                    jugadorId = event.jugador.jugadorId,
                    nombres = event.jugador.nombres,
                    partidas = event.jugador.partidas
                )
            }
        }
    }

    private fun insertarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.getAllJugadores().collect { lista ->
                if (lista.any { it.nombres.equals(jugador.nombres, ignoreCase = true) }) {
                    sendEvent("No se puede agregar un jugador con el mismo nombre")
                } else {
                    repository.insertJugador(jugador)
                }
            }
        }
    }

    private fun eliminarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.deleteJugador(jugador)
        }
    }
}