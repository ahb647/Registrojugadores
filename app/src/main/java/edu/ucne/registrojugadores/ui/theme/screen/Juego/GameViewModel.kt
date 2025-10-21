package edu.ucne.registrojugadores.ui.theme.screen.Juego

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrojugadores.Data.Repository.GestionHuacalesApiRepository
import edu.ucne.registrojugadores.Data.remote.dto.MovimientosRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GestionHuacalesApiRepository
) : ViewModel() {

    enum class Player { X, O }

    sealed class GameAction {
        data class BoardTapped(val cell: Int) : GameAction()
        object PlayAgain : GameAction()
    }

    data class GameState(
        val board: Array<Player?> = Array(9) { null },
        val currentPlayer: Player = Player.X,
        val hasWon: Boolean = false,
        val isDraw: Boolean = false,
        val lastPlayer: Player? = null,
        val xScore: Int = 0,
        val oScore: Int = 0,
        val drawScore: Int = 0,
        val isFinalizada: Boolean = false,
        val checkpointBoard: Array<Player?> = Array(9) { null }
    )

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()
    private var currentPartidaId: Int? = null

    fun iniciarPartida(partidaId: Int?) {
        currentPartidaId = partidaId
        partidaId?.let { cargarPartida(it) }
    }

    fun cargarPartida(partidaId: Int) {
        viewModelScope.launch {
            repository.getMovimientos(partidaId).onSuccess { lista ->
                val newBoard = Array<Player?>(9) { null }
                lista?.forEach { movimiento ->
                    val index = movimiento.posicionFila * 3 + movimiento.posicionColumna
                    newBoard[index] = if (movimiento.jugador == "X") Player.X else Player.O
                }
                _state.value = _state.value.copy(
                    board = newBoard,
                    checkpointBoard = newBoard.copyOf(),
                    isFinalizada = false
                )
                currentPartidaId = partidaId
            }.onFailure { e ->
                Log.e("GameViewModel", "Error cargando movimientos: ${e.message}")
            }
        }
    }

    fun guardarMovimientosActuales() {
        val currentState = _state.value
        val partidaId = currentPartidaId
        if (partidaId == null) {
            Log.e("GameViewModel", "No hay partida cargada para guardar")
            return
        }

        viewModelScope.launch {
            currentState.board.forEachIndexed { index, player ->
                player?.let {
                    val fila = index / 3
                    val col = index % 3
                    val request = MovimientosRequestDto(
                        partidaId = partidaId,
                        jugador = if (it == Player.X) "X" else "O",
                        posicionFila = fila,
                        posicionColumna = col
                    )
                    try {
                        repository.postMovimiento(request)
                    } catch (e: Exception) {
                        Log.e("GameViewModel", "Error guardando movimiento: ${e.message}")
                    }
                }
            }
            _state.value = currentState.copy(checkpointBoard = currentState.board.copyOf())
            Log.d("GameViewModel", "Movimientos guardados correctamente")
        }
    }

    fun rerollTablero() {
        val currentState = _state.value
        _state.value = currentState.copy(
            board = currentState.checkpointBoard.copyOf(),
            hasWon = false,
            isDraw = false,
            lastPlayer = null
        )
    }

    fun onAction(action: GameAction) {
        val currentState = _state.value
        when (action) {
            is GameAction.BoardTapped -> {
                if (currentState.board[action.cell] == null &&
                    !currentState.hasWon && !currentState.isDraw && !currentState.isFinalizada
                ) {
                    val newBoard = currentState.board.copyOf()
                    newBoard[action.cell] = currentState.currentPlayer
                    val hasWon = checkWin(newBoard, currentState.currentPlayer)
                    val isDraw = !hasWon && newBoard.all { it != null }
                    _state.value = currentState.copy(
                        board = newBoard,
                        currentPlayer = if (currentState.currentPlayer == Player.X) Player.O else Player.X,
                        hasWon = hasWon,
                        isDraw = isDraw,
                        lastPlayer = if (hasWon) currentState.currentPlayer else null,
                        isFinalizada = hasWon || isDraw
                    )
                }
            }
            GameAction.PlayAgain -> {
                _state.value = GameState(
                    xScore = currentState.xScore,
                    oScore = currentState.oScore,
                    drawScore = currentState.drawScore
                )
            }
        }
    }

    private fun checkWin(board: Array<Player?>, player: Player): Boolean {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        return winPatterns.any { pattern -> pattern.all { board[it] == player } }
    }
}
