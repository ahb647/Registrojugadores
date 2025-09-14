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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.ui.theme.util.Route
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: PartidasViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState()

    var partidaId by remember { mutableStateOf<Int?>(null) }
    val hoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    var fecha by remember { mutableStateOf(hoy) }
    var jugador1Id by remember { mutableStateOf(1) }
    var jugador2Id by remember { mutableStateOf(2) }
    var ganadorId by remember { mutableStateOf(0) }

    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Ninguno", "Jugador 1", "Jugador 2")
    var selectedOption by remember { mutableStateOf(opciones[0]) }

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

                // -------------------- FORMULARIO --------------------
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                // -------------------- DROPDOWN GANADOR --------------------
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedOption,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecciona Ganador") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opciones.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedOption = label
                                    expanded = false
                                    ganadorId = when (index) {
                                        1 -> jugador1Id
                                        2 -> jugador2Id
                                        else -> 0
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val nuevaPartida = Partida(
                            partidaId = partidaId ?: 0,
                            fecha = fecha,
                            jugador1Id = jugador1Id,
                            jugador2Id = jugador2Id,
                            ganadorId = ganadorId,
                            esFinalizada = ganadorId != 0
                        )
                        viewModel.insertarPartida(nuevaPartida)
                        Log.d("PartidasListScreen", "Partida guardada: $nuevaPartida")

                        // Limpiar formulario
                        partidaId = null
                        fecha = hoy
                        ganadorId = 0
                        selectedOption = opciones[0]
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = if (partidaId == null) "Guardar" else "Actualizar")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // -------------------- LISTA DE PARTIDAS --------------------
                if (partidas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay partidas registradas")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(partidas) { partida ->
                            PartidaItem(
                                partida = partida,
                                onDelete = { viewModel.eliminarPartida(partida) },
                                onSelect = {
                                    partidaId = partida.partidaId
                                    fecha = partida.fecha
                                    ganadorId = partida.ganadorId
                                    selectedOption = when (ganadorId) {
                                        jugador1Id -> opciones[1]
                                        jugador2Id -> opciones[2]
                                        else -> opciones[0]
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // -------------------- BOTÓN DE NAVEGACIÓN --------------------
            Button(
                onClick = { onNavigate(Route.JUGADOR_LIST) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
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
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
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
                    Text("Ganadaor: ${partida.ganadorId}")
                    Text("Estado: ${if (partida.esFinalizada) "Finalizada" else "En curso"}")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
