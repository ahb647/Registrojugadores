package edu.ucne.registrojugadores.ui.theme.screen.Logros_List

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrojugadores.Domain.Model.Model.Logros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogrosListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: LogrosListViewModel
) {
    val logros by viewModel.logros.collectAsState()
    var logroId by remember { mutableStateOf<Int?>(null) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Logros Registrados",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Logro") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isBlank()) {
                            Log.e("LogrosListScreen", "El nombre del logro no puede estar vacío")
                            return@Button
                        }

                        val existe = logros.any { it.logroNombre.equals(nombre, ignoreCase = true) && it.logroId != logroId }
                        if (existe) {
                            Log.e("LogrosListScreen", "Ya existe un logro con el nombre: $nombre")
                            return@Button
                        }

                        if (logroId == null) {
                            viewModel.insertarLogro(
                                Logros(
                                    logroNombre = nombre,
                                    descripcion = descripcion
                                )
                            )
                            Log.d("LogrosListScreen", "Logro insertado: $nombre")
                        } else {
                            viewModel.actualizarLogro(
                                Logros(
                                    logroId = logroId,
                                    logroNombre = nombre,
                                    descripcion = descripcion
                                )
                            )
                            Log.d("LogrosListScreen", "Logro actualizado: $nombre")
                        }

                        logroId = null
                        nombre = ""
                        descripcion = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = if (logroId == null) "Guardar" else "Actualizar")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (logros.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay logros registrados")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(logros) { logro ->
                            LogroItem(
                                logro = logro,
                                onDelete = { viewModel.eliminarLogro(logro) },
                                onSelect = {
                                    logroId = logro.logroId
                                    nombre = logro.logroNombre
                                    descripcion = logro.descripcion
                                    Log.d("LogrosListScreen", "Logro seleccionado para editar: ${logro.logroNombre}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogroItem(
    logro: Logros,
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
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Logro",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = logro.logroNombre,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = logro.descripcion,
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
