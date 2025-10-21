package edu.ucne.registrojugadores.ui.theme.screen.Juego

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.ui.theme.GrayBackground

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    jugadoresViewModel: edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresViewModel = hiltViewModel()
) {
    val state by gameViewModel.state.collectAsState()
    val jugadoresDto by jugadoresViewModel.jugadores.collectAsState(initial = emptyList())
    val jugadores: List<Jugadores> = jugadoresDto.map { Jugadores(it.jugadorId, it.nombres) }

    var searchPartidaId by remember { mutableStateOf("") }
    val numberColor = Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchPartidaId,
                onValueChange = { searchPartidaId = it },
                label = { Text("ID Partida") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(56.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Gray,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )
            )

            OutlinedButton(
                onClick = {
                    searchPartidaId.toIntOrNull()?.let { partidaId ->
                        gameViewModel.iniciarPartida(partidaId)
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Cargar partida",
                    tint = Color.Black
                )
            }

            OutlinedButton(
                onClick = { gameViewModel.guardarMovimientosActuales() },
                enabled = state.board.any { it != null } && state.board.any { it != null },
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Guardar movimiento",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        GameBoard(
            board = state.board.map { player ->
                when (player) {
                    GameViewModel.Player.X -> Player.X
                    GameViewModel.Player.O -> Player.O
                    null -> null
                }
            }.toTypedArray(),
            onCellClicked = { cell ->
                if (!state.hasWon && !state.isDraw && !state.isFinalizada) {
                    gameViewModel.onAction(GameViewModel.GameAction.BoardTapped(cell))
                }
            },
            colorX = Color.Red,
            colorO = Color.Blue,
            numberColor = numberColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when {
                state.hasWon -> "Ganador: ${state.lastPlayer?.name ?: ""}"
                state.isDraw -> "¡Empate!"
                else -> "Turno de: ${state.currentPlayer.name}"
            },
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}
