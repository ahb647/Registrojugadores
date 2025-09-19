package edu.ucne.registrojugadores.ui.theme.screen.Logros_List

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Logros
import edu.ucne.registrojugadores.Domain.Model.Repository.LogrosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogrosListViewModel @Inject constructor(
    private val repository: LogrosRepository
) : ViewModel() {

    private val _logros = MutableStateFlow<List<Logros>>(emptyList())
    val logros: StateFlow<List<Logros>> = _logros.asStateFlow()

    private val _logroSeleccionado = MutableStateFlow<Logros?>(null)
    val logroSeleccionado: StateFlow<Logros?> = _logroSeleccionado.asStateFlow()

    init {
        obtenerTodosLogros()
    }

    private fun obtenerTodosLogros() {
        viewModelScope.launch {
            repository.getAllLogros().collectLatest { lista ->
                _logros.value = lista
            }
        }
    }

    fun obtenerLogroPorId(id: Int) {
        viewModelScope.launch {
            _logroSeleccionado.value = repository.getLogroById(id)
        }
    }

    fun insertarLogro(logro: Logros) {
        viewModelScope.launch {
            val existe = _logros.value.any { it.logroNombre.equals(logro.logroNombre, ignoreCase = true) }
            if (existe) {
                Log.d("LogrosListViewModel", "No se puede agregar un logro con el mismo nombre: ${logro.logroNombre}")
            } else {
                repository.insertLogro(logro)
            }
        }
    }

    fun actualizarLogro(logro: Logros) {
        viewModelScope.launch {
            repository.updateLogro(logro)
        }
    }

    fun eliminarLogro(logro: Logros) {
        viewModelScope.launch {
            repository.deleteLogro(logro)
        }
    }
}
