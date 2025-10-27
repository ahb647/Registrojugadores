package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase

import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.flow.first

class ValidarJugadorUseCase(
    private val repository: JugadoresRepository
) {
    suspend operator fun invoke(jugador: Jugadores): Result<Unit> {
        if (jugador.nombres.isBlank() || jugador.email.isBlank()) {
            return Result.failure(Exception("Nombre o email no pueden estar vacíos"))
        }
        val jugadores = repository.getAllJugadores().first()
        val nombreRepetido = jugadores.any {
            it.nombres.equals(jugador.nombres, ignoreCase = true) &&
                    it.jugadorId != jugador.jugadorId
        }
        if (nombreRepetido) {
            return Result.failure(Exception("Ya existe un jugador con ese nombre"))
        }
        val emailRepetido = jugadores.any {
            it.email.equals(jugador.email, ignoreCase = true) &&
                    it.jugadorId != jugador.jugadorId
        }
        if (emailRepetido) {
            return Result.failure(Exception("Ya existe un jugador con ese email"))
        }
        return Result.success(Unit)
    }
}