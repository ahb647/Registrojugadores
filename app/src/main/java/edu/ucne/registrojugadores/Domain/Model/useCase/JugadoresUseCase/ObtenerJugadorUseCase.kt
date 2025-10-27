package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase

import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository

class ObtenerJugadorUseCase(
    private val repository: JugadoresRepository
) {
    suspend operator fun invoke(id: Int): Jugadores? {
        return repository.getJugadorById(id)
    }
}