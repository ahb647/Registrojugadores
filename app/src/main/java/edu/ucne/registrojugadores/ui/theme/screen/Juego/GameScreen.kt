package edu.ucne.registrojugadores.ui.theme.screen.Juego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.ui.theme.BlueCustom
import edu.ucne.registrojugadores.ui.theme.GrayBackground
import edu.ucne.registrojugadores.ui.theme.screen.Partidas.PartidasViewModel
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    jugadorXId: Int?,
    jugadorOId: Int?,
    colorX: Color = Color.Red,
    colorO: Color = Color.Blue,
    onPartidaTerminada: () -> Unit = {},
    onExitGame: () -> Unit = {},
    gameViewModel: GameViewModel = hiltViewModel(),
    jugadoresViewModel: edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresViewModel = hiltViewModel(),
    partidasViewModel: PartidasViewModel = hiltViewModel()
) {
    val state by gameViewModel.state.collectAsState()
    val jugadores: List<Jugadores> by jugadoresViewModel.jugadores.collectAsState(initial = emptyList())
    val jugadorX = remember(jugadorXId, jugadores) { jugadores.find { it.jugadorId == jugadorXId } }
    val jugadorO = remember(jugadorOId, jugadores) { jugadores.find { it.jugadorId == jugadorOId } }

    if (jugadorX == null || jugadorO == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Error: Jugadores no encontrados")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onExitGame) { Text("Volver") }
        }
        return
    }

    val scope = rememberCoroutineScope()

    // Guardar la partida al terminar (ganador o empate)
    LaunchedEffect(state.hasWon, state.isDraw) {
        if (state.hasWon || state.isDraw) {
            val ganadorId = when {
                state.hasWon -> when (state.lastPlayer) {
                    Player.X -> jugadorX.jugadorId ?: 0
                    Player.O -> jugadorO.jugadorId ?: 0
                    else -> 0
                }
                state.isDraw -> 0 // Empate
                else -> 0
            }

            scope.launch {
                val partida = Partida(
                    partidaId = 0,
                    fecha = java.time.LocalDate.now().toString(),
                    jugador1Id = jugadorX.jugadorId ?: 0,
                    jugador2Id = jugadorO.jugadorId ?: 0,
                    ganadorId = ganadorId,
                    esFinalizada = true
                )
                partidasViewModel.onEvent(
                    edu.ucne.registrojugadores.ui.theme.screen.Partidas.PartidasEvent.Save(partida)
                )

                // Solo incrementar partidas jugadas para ambos jugadores
                gameViewModel.incrementarPartidas(jugadorX.jugadorId ?: 0)
                gameViewModel.incrementarPartidas(jugadorO.jugadorId ?: 0)

                onPartidaTerminada()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        PuntuacionRow(state, jugadorX, jugadorO)
        TituloJuego()
        TableroJuego(state, gameViewModel, colorX, colorO)
        MensajeTurno(state, jugadorX, jugadorO)
        BotonesJuego(onExitGame, gameViewModel)
    }
}

@Composable
private fun PuntuacionRow(state: GameState, jugadorX: Jugadores, jugadorO: Jugadores) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${jugadorO.nombres} : ${state.oScore}", fontSize = 16.sp)
        Text("Empates : ${state.drawScore}", fontSize = 16.sp)
        Text("${jugadorX.nombres} : ${state.xScore}", fontSize = 16.sp)
    }
}

@Composable
private fun TituloJuego() {
    Text(
        text = "Tic Tac Toe",
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive,
        color = BlueCustom
    )
}

@Composable
private fun TableroJuego(state: GameState, gameViewModel: GameViewModel, colorX: Color, colorO: Color) {
    GameBoard(
        board = state.board,
        onCellClicked = { cell ->
            if (!state.hasWon && !state.isDraw) {
                gameViewModel.onAction(GameAction.BoardTapped(cell))
            }
        },
        colorX = colorX,
        colorO = colorO
    )
}

@Composable
private fun MensajeTurno(state: GameState, jugadorX: Jugadores, jugadorO: Jugadores) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = when {
                state.hasWon -> "¡${if (state.lastPlayer == Player.X) jugadorX.nombres else jugadorO.nombres} Ganaste!"
                state.isDraw -> "¡Es un empate!"
                else -> "Turno de: ${if (state.currentPlayer == Player.X) jugadorX.nombres else jugadorO.nombres}"
            },
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun BotonesJuego(onExitGame: () -> Unit, gameViewModel: GameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onExitGame,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("Salir") }

        Button(
            onClick = { gameViewModel.onAction(GameAction.PlayAgain) },
            colors = ButtonDefaults.buttonColors(containerColor = BlueCustom)
        ) { Text("Volver a Jugar") }
    }
}
