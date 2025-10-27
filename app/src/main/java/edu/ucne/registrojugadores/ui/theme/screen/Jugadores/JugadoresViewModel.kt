package edu.ucne.registrojugadores.ui.theme.screen.Jugadores


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Data.remote.Resource
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

    val jugadores: StateFlow<List<Jugadores>> =
        repository.getAllJugadores().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _event = Channel<String>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(message: String) {
        viewModelScope.launch {
            _event.send(message)
        }
    }

    init {
        syncJugadores()
    }

    fun syncJugadores() {
        viewModelScope.launch {
            when (val result = repository.syncJugadores()) {
                is Resource.Success -> sendEvent("Jugadores sincronizados")
                is Resource.Error -> sendEvent("Error al sincronizar: ${result.message}")
                is Resource.Loading -> {}
            }
        }
    }

    fun crearJugador(nombre: String, email: String) {
        viewModelScope.launch {
            val nuevoJugador = Jugadores(
                jugadorId = 0,
                nombres = nombre,
                email = email
            )
            repository.insertJugador(nuevoJugador)
            sendEvent("Jugador creado correctamente")
        }
    }

    fun eliminarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.deleteJugador(jugador)
            sendEvent("Jugador eliminado correctamente")
        }
    }

    fun actualizarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.updateJugador(jugador)
            sendEvent("Jugador actualizado correctamente")
        }
    }
}