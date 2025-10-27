package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase



data class JugadorUseCases(
    val validarJugador: ValidarJugadorUseCase,
    val guardarJugador: GuardarJugadorUseCase,
    val eliminarJugador: EliminarJugadorUseCase,
    val obtenerJugador: ObtenerJugadorUseCase,
    val obtenerJugadores: ObtenerJugadoresUseCase
)