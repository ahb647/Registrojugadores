package edu.ucne.registrojugadores.ui.theme.screen.Logros

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrojugadores.Domain.Model.Model.Logros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogrosScreen(
    state: LogrosState,
    onEvent: (LogrosEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Logro") },
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
                value = state.logroNombre,
                onValueChange = { onEvent(LogrosEvent.LogroNombreChanged(it)) },
                label = { Text("Nombre del logro") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(LogrosEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onEvent(
                        LogrosEvent.Save(
                            logro = Logros(
                                logroId = state.logroId,
                                logroNombre = state.logroNombre,
                                descripcion = state.descripcion
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
