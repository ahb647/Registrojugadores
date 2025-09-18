package edu.ucne.registrojugadores.ui.theme.screen.Partidas_List

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.ui.theme.util.Route
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores_List.JugadoresViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: PartidasViewModel = hiltViewModel(),
    jugadoresViewModel: JugadoresViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState()
    val jugadores by jugadoresViewModel.jugadores.collectAsState(initial = emptyList())

    val hoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    var fecha by remember { mutableStateOf(hoy) }

    var jugador1Id by remember { mutableStateOf<Int?>(null) }
    var jugador2Id by remember { mutableStateOf<Int?>(null) }

    var expandedJugador1 by remember { mutableStateOf(false) }
    var expandedJugador2 by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Partidas Registradas", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // FECHA
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // JUGADOR 1
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

                // JUGADOR 2
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

                // LISTA DE PARTIDAS
                if (partidas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                onDelete = { viewModel.eliminarPartida(partida) },
                                onSelect = {
                                    jugador1Id = partida.jugador1Id
                                    jugador2Id = partida.jugador2Id
                                }
                            )
                        }
                    }
                }
            }

            // BOTÓN FLOTANTE PARA JUGAR
            if (jugador1Id != null && jugador2Id != null) {
                FloatingActionButton(
                    onClick = {
                        val route = "${Route.GAME_SCREEN}?jugadorXId=$jugador1Id&jugadorOId=$jugador2Id"
                        Log.d("PartidasListScreen", "Navegando a $route")
                        onNavigate(route)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = "Jugar")
                }
            }

            // BOTÓN NAVEGAR A JUGADORES
            Button(
                onClick = { onNavigate(Route.JUGADOR_LIST) },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(64.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text("▶")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaItem(
    partida: Partida,
    jugadores: List<Jugadores>,
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
    // Mostrar "Empate" si la partida terminó con ganadorId = 0
    val ganadorNombre = when {
        partida.esFinalizada && partida.ganadorId == 0 -> "Empate"
        partida.ganadorId != 0 -> jugadores.firstOrNull { it.jugadorId == partida.ganadorId }?.nombres ?: "Desconocido"
        else -> "Ninguno"
    }

    val estado = if (partida.esFinalizada) "Finalizada" else "En curso"

    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = "Partida",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text("PartidaID: ${partida.partidaId}", fontWeight = FontWeight.Bold)
                    Text("Fecha: ${partida.fecha}")
                    Text("Ganador: $ganadorNombre")
                    Text("Estado: $estado")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPartidasListScreen() {
    val mockJugadores = listOf(
        Jugadores(jugadorId = 1, nombres = "Alice", partidas = 3),
        Jugadores(jugadorId = 2, nombres = "Bob", partidas = 5)
    )
    val mockPartidas = listOf(
        Partida(1, "2025-09-10", 1, 2, 1, true),
        Partida(2, "2025-09-11", 2, 1, 2, true),
        Partida(3, "2025-09-12", 1, 2, 0, true) // Empate
    )

    MaterialTheme {
        Column {
            mockPartidas.forEach {
                PartidaItem(it, mockJugadores, {}, {})
            }
        }
    }
}
