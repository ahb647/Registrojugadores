package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase



import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository

class EliminarJugadorUseCase(
    private val repository: JugadoresRepository
) {
    suspend operator fun invoke(jugador: Jugadores) {
        repository.deleteJugador(jugador)
    }
}