package edu.ucne.registrojugadores.ui.theme.screen.Partidas_List

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.Domain.Model.Repository.PartidasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartidasViewModel @Inject constructor(
    private val repository: PartidasRepository
) : ViewModel() {


    private val _partidas = MutableStateFlow<List<Partida>>(emptyList())
    val partidas: StateFlow<List<Partida>> = _partidas.asStateFlow()


    private val _partidaSeleccionada = MutableStateFlow<Partida?>(null)
    val partidaSeleccionada: StateFlow<Partida?> = _partidaSeleccionada.asStateFlow()

    init {
        obtenerTodasPartidas()
    }


    private fun obtenerTodasPartidas() {
        viewModelScope.launch {
            repository.getAllPartidas().collectLatest { lista ->
                _partidas.value = lista
            }
        }
    }

    fun obtenerPartidaPorId(id: Int) {
        viewModelScope.launch {
            _partidaSeleccionada.value = repository.getPartidaById(id)
        }
    }


    fun insertarPartida(partida: Partida) {
        viewModelScope.launch {
            val finalizado = partida.ganadorId != 0
            val partidaActualizada = partida.copy(esFinalizada = finalizado)
            repository.insertPartida(partidaActualizada)
            Log.d("PartidasViewModel", "Partida insertada: $partidaActualizada")
        }
    }


    fun actualizarPartida(partida: Partida) {
        viewModelScope.launch {
            val finalizado = partida.ganadorId != 0
            val partidaActualizada = partida.copy(esFinalizada = finalizado)
            repository.updatePartida(partidaActualizada)
            Log.d("PartidasViewModel", "Partida actualizada: $partidaActualizada")
        }
    }


    fun eliminarPartida(partida: Partida) {
        viewModelScope.launch {
            repository.deletePartida(partida)
            Log.d("PartidasViewModel", "Partida eliminada: $partida")
        }
    }
}
