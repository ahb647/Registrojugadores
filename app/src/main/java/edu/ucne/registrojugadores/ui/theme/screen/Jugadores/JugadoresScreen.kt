package edu.ucne.registrojugadores.ui.theme.screen.Jugadores



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorScreen(
    jugador: Jugadores?,
    onEvent: (JugadoresEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by remember(jugador) {
        mutableStateOf(
            JugadoresState(
                jugadorId = jugador?.jugadorId,
                nombres = jugador?.nombres ?: "",
                email = jugador?.email ?: ""
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Jugador") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                value = state.email,
                onValueChange = { onEvent(JugadoresEvent.EmailChanged(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onEvent(
                        JugadoresEvent.Save(
                            jugador = Jugadores(
                                jugadorId = state.jugadorId ?: 0,
                                nombres = state.nombres,
                                email = state.email
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