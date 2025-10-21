package edu.ucne.registrojugadores.ui.theme.screen.Jugadores_List

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrojugadores.Data.remote.dto.JugadorResponseDto
import edu.ucne.registrojugadores.ui.theme.util.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadoresListScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresViewModel
) {
    val jugadores by viewModel.jugadores.collectAsState()
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isBlank() || email.isBlank()) {
                            Log.e("JugadoresListScreen", "Nombre y email son obligatorios")
                            return@Button
                        }
                        viewModel.crearJugador(nombre, email)
                        nombre = ""
                        email = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = "Guardar")
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
                            JugadorItem(jugador)
                        }
                    }
                }
            }

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
        }
    }
}

@Composable
fun JugadorItem(jugador: JugadorResponseDto) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = "Email: ${jugador.email}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
