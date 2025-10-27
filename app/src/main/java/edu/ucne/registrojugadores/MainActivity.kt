package edu.ucne.registrojugadores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrojugadores.ui.theme.RegistrojugadoresTheme
import edu.ucne.registrojugadores.ui.theme.screen.Juego.GameScreen
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadorScreen
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresEvent
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores.JugadoresViewModel
import edu.ucne.registrojugadores.ui.theme.screen.Jugadores_List.JugadoresListScreen
import edu.ucne.registrojugadores.ui.theme.screen.Partidas.PartidasViewModel
import edu.ucne.registrojugadores.ui.theme.screen.Partidas_List.PartidasListScreen
import edu.ucne.registrojugadores.ui.theme.screen.Logros_List.LogrosListScreen
import edu.ucne.registrojugadores.ui.theme.screen.Logros_List.LogrosListViewModel
import edu.ucne.registrojugadores.ui.theme.util.Route

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            RegistrojugadoresTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Registro de Jugadores") }) },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = Route.JUGADOR_LIST,
                        modifier = Modifier.padding(paddingValues)
                    ) {

                        composable(Route.JUGADOR_LIST) {
                            val viewModel: JugadoresViewModel = hiltViewModel()
                            val backEntry by navController.currentBackStackEntryAsState()
                            val savedStateHandle = backEntry?.savedStateHandle

                            val msgFlow = remember(savedStateHandle) {
                                savedStateHandle?.getStateFlow("snackbar_msg", "")
                            }
                            val msgFromForm by msgFlow?.collectAsState() ?: remember { mutableStateOf("") }

                            LaunchedEffect(msgFromForm) {
                                if (msgFromForm.isNotEmpty()) {
                                    snackbarHostState.showSnackbar(msgFromForm)
                                    savedStateHandle?.set("snackbar_msg", "")
                                }
                            }

                            JugadoresListScreen(
                                viewModel = viewModel,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable(Route.JUGADOR_FORM) {
                            val viewModel: JugadoresViewModel = hiltViewModel()
                            val jugadores by viewModel.jugadores.collectAsState(initial = emptyList())
                            val jugador = jugadores.firstOrNull()

                            JugadorScreen(
                                jugador = jugador,
                                onEvent = { event ->
                                    if (event is JugadoresEvent.Save) {
                                        navController.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("snackbar_msg", "Jugador guardado")
                                        navController.popBackStack()
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(Route.PARTIDA_LIST) {
                            val viewModel: PartidasViewModel = hiltViewModel()
                            val jugadoresViewModel: JugadoresViewModel = hiltViewModel()
                            PartidasListScreen(
                                viewModel = viewModel,
                                jugadoresViewModel = jugadoresViewModel,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable(Route.LOGROS_LIST) {
                            val viewModel: LogrosListViewModel = hiltViewModel()
                            LogrosListScreen(
                                viewModel = viewModel,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable(
                            route = "${Route.GAME_SCREEN}?jugadorXId={jugadorXId}&jugadorOId={jugadorOId}&partidaId={partidaId}",
                            arguments = listOf(
                                navArgument("jugadorXId") { type = NavType.IntType; defaultValue = 0 },
                                navArgument("jugadorOId") { type = NavType.IntType; defaultValue = 0 },
                                navArgument("partidaId") { type = NavType.IntType; defaultValue = 0 }
                            )
                        ) {
                            GameScreen(
                                gameViewModel = hiltViewModel(),
                                jugadoresViewModel = hiltViewModel()
                            )
                        }
                    }
                }
            }
        }
    }
}