package edu.ucne.registrojugadores.ui.theme.screen.Jugadores_List


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JugadoresViewModel @Inject constructor(
    private val repository: JugadoresRepository
) : ViewModel() {

    private val _jugadores = MutableStateFlow<List<Jugadores>>(emptyList())
    val jugadores: StateFlow<List<Jugadores>> = _jugadores.asStateFlow()

    private val _jugadorSeleccionado = MutableStateFlow<Jugadores?>(null)
    val jugadorSeleccionado: StateFlow<Jugadores?> = _jugadorSeleccionado.asStateFlow()

    init {
        obtenerTodosJugadores()
    }

    private fun obtenerTodosJugadores() {
        viewModelScope.launch {
            repository.getAllJugadores().collectLatest { lista ->
                _jugadores.value = lista
            }
        }
    }

    fun obtenerJugadorPorId(id: Int) {
        viewModelScope.launch {
            _jugadorSeleccionado.value = repository.getJugadorById(id)
        }
    }

    fun insertarJugador(jugador: Jugadores) {
        viewModelScope.launch {

            val existe = _jugadores.value.any { it.nombres.equals(jugador.nombres, ignoreCase = true) }
            if (existe) {
                Log.d("JugadoresViewModel", "No se puede agregar un jugador con el mismo nombre: ${jugador.nombres}")
            } else {
                repository.insertJugador(jugador)
            }
        }
    }

    fun actualizarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.updateJugador(jugador)
        }
    }

    fun eliminarJugador(jugador: Jugadores) {
        viewModelScope.launch {
            repository.deleteJugador(jugador)
        }
    }
}