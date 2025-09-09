package edu.ucne.registrojugadores.ui.theme.screen.Jugadores



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresEvent
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorScreen(
    state: JugadoresState,
    onEvent: (JugadoresEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Jugador") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "navigate back"
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.nombres,
                onValueChange = { onEvent(JugadoresEvent.NombreChanged(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.partidas.toString(),
                onValueChange = {
                    val partidas = it.toIntOrNull() ?: 0
                    onEvent(JugadoresEvent.PartidasChanged(partidas))
                },
                label = { Text("Partidas") },
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    onEvent(
                        JugadoresEvent.Save(
                            jugador = Jugadores(
                                jugadorId = state.jugadorId,
                                nombres = state.nombres,
                                partidas = state.partidas
                            )
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = "Guardar")
            }
        }
    }
}