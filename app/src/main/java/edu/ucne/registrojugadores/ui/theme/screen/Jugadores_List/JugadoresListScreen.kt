package edu.ucne.registrojugadores.ui.theme.screen.Jugadores_List

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.ui.theme.util.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadoresListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: JugadoresViewModel
) {
    val jugadores by viewModel.jugadores.collectAsState(emptyList())

    var jugadorId by remember { mutableStateOf<Int?>(null) }
    var nombre by remember { mutableStateOf("") }
    var partidas by remember { mutableStateOf("0") }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Jugadores Registrados",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = partidas,
                    onValueChange = { partidas = it },
                    label = { Text("Partidas") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isBlank()) {
                            Log.e("JugadoresListScreen", "El nombre no puede estar vacío")
                            return@Button
                        }

                        val partidasInt = partidas.toIntOrNull() ?: 0

                        val existe = jugadores.any { it.nombres.equals(nombre, ignoreCase = true) && it.jugadorId != jugadorId }
                        if (existe) {
                            Log.e("JugadoresListScreen", "Ya existe un jugador con el nombre: $nombre")
                            return@Button
                        }

                        if (jugadorId == null) {
                            viewModel.insertarJugador(
                                Jugadores(
                                    nombres = nombre,
                                    partidas = partidasInt
                                )
                            )
                            Log.d("JugadoresListScreen", "Jugador insertado: $nombre con $partidasInt partidas")
                        } else {
                            viewModel.actualizarJugador(
                                Jugadores(
                                    jugadorId = jugadorId!!,
                                    nombres = nombre,
                                    partidas = partidasInt
                                )
                            )
                            Log.d("JugadoresListScreen", "Jugador actualizado: $nombre con $partidasInt partidas")
                        }

                        jugadorId = null
                        nombre = ""
                        partidas = "0"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = if (jugadorId == null) "Guardar" else "Actualizar")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (jugadores.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay jugadores registrados")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(jugadores) { jugador ->
                            JugadorItem(
                                jugador = jugador,
                                onDelete = { viewModel.eliminarJugador(jugador) },
                                onSelect = {
                                    jugadorId = jugador.jugadorId
                                    nombre = jugador.nombres
                                    partidas = jugador.partidas.toString()
                                    Log.d("JugadoresListScreen", "Jugador seleccionado para editar: ${jugador.nombres}")
                                }
                            )
                        }
                    }
                }
            }

            // Botón de Partidas en la esquina inferior derecha
            Button(
                onClick = { onNavigate(Route.PARTIDA_LIST) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(64.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text("▶")
            }

            // Botón de Logros centrado en la parte inferior
            Button(
                onClick = { onNavigate(Route.LOGROS_LIST) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .height(48.dp)
                    .width(120.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Logros")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorItem(
    jugador: Jugadores,
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
                    imageVector = Icons.Default.Person,
                    contentDescription = "Jugador",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = jugador.nombres,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Partidas: ${jugador.partidas}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
