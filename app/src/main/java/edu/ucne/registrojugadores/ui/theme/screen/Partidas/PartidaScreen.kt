package edu.ucne.registrojugadores.ui.theme.screen.Partidas

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasScreen(viewModel: PartidasViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Partidas", fontSize = 24.sp, fontWeight = FontWeight.Bold) })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.partidas) { partida ->
                    PartidaItem(
                        partida = partida,
                        onDelete = { viewModel.onEvent(PartidasEvent.Delete(partida)) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaItem(
    partida: Partida,
    onDelete: () -> Unit
) {
    Card(
        onClick = { /* Solo diseño */ },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
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
                    Text("Jugador1ID: ${partida.jugador1Id}")
                    Text("Jugador2ID: ${partida.jugador2Id}")
                    Text("GanadorID: ${partida.ganadorId}")
                    Text("Estado: ${if (partida.esFinalizada) "Finalizada" else "En curso"}")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
