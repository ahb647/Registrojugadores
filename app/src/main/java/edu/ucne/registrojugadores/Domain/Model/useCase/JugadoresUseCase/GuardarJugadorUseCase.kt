package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase


import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository

class GuardarJugadorUseCase(
    private val repository: JugadoresRepository,
    private val validarJugador: ValidarJugadorUseCase
) {
    suspend operator fun invoke(jugador: Jugadores): Result<Boolean> {
        val validacion = validarJugador(jugador)
        if (validacion.isFailure) return Result.failure(validacion.exceptionOrNull()!!)
        if (jugador.jugadorId == 0) {
            repository.insertJugador(jugador)
        } else {
            repository.updateJugador(jugador)
        }
        return Result.success(true)
    }
}