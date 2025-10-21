package edu.ucne.registrojugadores.ui.theme.screen.Jugadores

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Data.Repository.GestionHuacalesApiRepository
import edu.ucne.registrojugadores.Data.remote.dto.JugadorRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.JugadorResponseDto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadoresViewModel @Inject constructor(
    private val repository: GestionHuacalesApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _jugadores = MutableStateFlow<List<JugadorResponseDto>>(emptyList())
    val jugadores: StateFlow<List<JugadorResponseDto>> = _jugadores.asStateFlow()

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    init {
        cargarJugadores()
    }

    fun cargarJugadores() {
        viewModelScope.launch {
            val result = repository.getJugadores()
            result.onSuccess { lista ->
                _jugadores.value = lista ?: emptyList()
            }.onFailure { e ->
                sendEvent("Error al cargar jugadores: ${e.message}")
            }
        }
    }

    fun crearJugador(nombre: String, email: String) {
        viewModelScope.launch {
            val request = JugadorRequestDto(
                nombres = nombre,
                email = email
            )
            val result = repository.postJugador(request)
            result.onSuccess { nuevoJugador ->
                if (nuevoJugador != null) {
                    _jugadores.value = _jugadores.value + nuevoJugador
                    sendEvent("Jugador creado correctamente")
                } else {
                    sendEvent("No se pudo crear el jugador")
                }
            }.onFailure { e ->
                sendEvent("Error al crear jugador: ${e.message}")
            }

        }
    }
}