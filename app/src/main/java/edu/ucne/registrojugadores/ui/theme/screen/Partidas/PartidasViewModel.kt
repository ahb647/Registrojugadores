package edu.ucne.registrojugadores.ui.theme.screen.Partidas


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Data.Repository.GestionHuacalesApiRepository
import edu.ucne.registrojugadores.Data.remote.dto.PartidaRequestDto
import edu.ucne.registrojugadores.Data.remote.dto.PartidaResponseDto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartidasViewModel @Inject constructor(
    private val repository: GestionHuacalesApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _partidas = MutableStateFlow<List<PartidaResponseDto>>(emptyList())
    val partidas: StateFlow<List<PartidaResponseDto>> = _partidas.asStateFlow()

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    init {
        cargarPartidas()
    }

    fun cargarPartidas() {
        viewModelScope.launch {
            val result = repository.getPartidas()
            result.onSuccess { lista ->
                _partidas.value = lista ?: emptyList()
            }.onFailure { e ->
                sendEvent("Error al cargar partidas: ${e.message}")
            }
        }
    }

    fun crearPartida(jugador1Id: Int, jugador2Id: Int) {
        viewModelScope.launch {
            val request = PartidaRequestDto(
                jugador1Id = jugador1Id,
                jugador2Id = jugador2Id
            )
            val result = repository.postPartida(request)
            result.onSuccess { nuevaPartida ->
                if (nuevaPartida != null) {
                    _partidas.value = _partidas.value + nuevaPartida
                    sendEvent("Partida creada correctamente")
                } else {
                    sendEvent("No se pudo crear la partida")
                }
            }.onFailure { e ->
                sendEvent("Error al crear partida: ${e.message}")
            }
        }
    }
}