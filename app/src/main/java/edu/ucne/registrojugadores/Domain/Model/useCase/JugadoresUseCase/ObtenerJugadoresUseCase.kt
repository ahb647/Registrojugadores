package edu.ucne.registrojugadores.Domain.Model.useCase.JugadoresUseCase

import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerJugadoresUseCase @Inject constructor(
    private val repository: JugadoresRepository
) {
    operator fun invoke(): Flow<List<Jugadores>> {
        return repository.getAllJugadores()
    }
}