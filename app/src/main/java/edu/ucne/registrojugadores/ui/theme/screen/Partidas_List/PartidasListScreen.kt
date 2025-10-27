package edu.ucne.registrojugadores.ui.theme.screen.Partidas_List


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadores.Data.remote.dto.PartidaResponseDto
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresViewModel
import edu.ucne.registrojugadores.ui.theme.screen.Partidas.PartidasViewModel
import edu.ucne.registrojugadores.ui.theme.util.Route
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: PartidasViewModel = hiltViewModel(),
    jugadoresViewModel: JugadoresViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState(initial = emptyList())
    val jugadores by jugadoresViewModel.jugadores.collectAsState(initial = emptyList())

    val hoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    var fecha by remember { mutableStateOf(hoy) }

    var jugador1Id by remember { mutableStateOf<Int?>(null) }
    var jugador2Id by remember { mutableStateOf<Int?>(null) }

    var expandedJugador1 by remember { mutableStateOf(false) }
    var expandedJugador2 by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Partidas Registradas", fontSize = 24.sp, fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedJugador1,
                    onExpandedChange = { expandedJugador1 = !expandedJugador1 }
                ) {
                    OutlinedTextField(
                        value = jugadores.firstOrNull { it.jugadorId == jugador1Id }?.nombres ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jugador 1") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedJugador1) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedJugador1,
                        onDismissRequest = { expandedJugador1 = false }
                    ) {
                        jugadores.forEach { jugador ->
                            DropdownMenuItem(
                                text = { Text(jugador.nombres) },
                                onClick = {
                                    jugador1Id = jugador.jugadorId
                                    expandedJugador1 = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedJugador2,
                    onExpandedChange = { expandedJugador2 = !expandedJugador2 }
                ) {
                    OutlinedTextField(
                        value = jugadores.firstOrNull { it.jugadorId == jugador2Id }?.nombres ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jugador 2") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedJugador2) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedJugador2,
                        onDismissRequest = { expandedJugador2 = false }
                    ) {
                        jugadores.forEach { jugador ->
                            DropdownMenuItem(
                                text = { Text(jugador.nombres) },
                                onClick = {
                                    jugador2Id = jugador.jugadorId
                                    expandedJugador2 = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (partidas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay partidas registradas")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(partidas) { partida ->
                            PartidaItem(
                                partida = partida,
                                jugadores = jugadores,
                                onSelect = {
                                    jugador1Id = partida.jugador1Id
                                    jugador2Id = partida.jugador2Id
                                }
                            )
                        }
                    }
                }
            }

            if (jugador1Id != null && jugador2Id != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.crearPartida(jugador1Id!!, jugador2Id!!)
                        val route = "${Route.GAME_SCREEN}?jugadorXId=$jugador1Id&jugadorOId=$jugador2Id"
                        Log.d("PartidasListScreen", "Navegando a $route")
                        onNavigate(route)
                    },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = "Jugar")
                }
            }
        }
    }
}

private fun LazyItemScope.PartidaItem(
    partida: PartidaResponseDto,
    jugadores: List<Jugadores>,
    onSelect: () -> Unit
) {
}

@Composable
fun PartidaItem(
    partida: Partida,
    jugadores: List<Jugadores>,
    onSelect: () -> Unit
) {
    val jugador1Nombre = jugadores.firstOrNull { it.jugadorId == partida.jugador1Id }?.nombres ?: "Jugador 1"
    val jugador2Nombre = jugadores.firstOrNull { it.jugadorId == partida.jugador2Id }?.nombres ?: "Jugador 2"

    Card(onClick = onSelect, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("PartidaID: ${partida.partidaId}", fontWeight = FontWeight.Bold)
            Text("Jugador 1: $jugador1Nombre")
            Text("Jugador 2: $jugador2Nombre")
        }
    }
}