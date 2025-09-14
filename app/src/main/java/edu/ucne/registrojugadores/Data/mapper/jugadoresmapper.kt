package edu.ucne.registrojugadores.Data.mapper

import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import edu.ucne.registrojugadores.Data.Local.Entity.PartidaEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Model.Partida


// Entity -> Dominio
fun JugadorEntity.asExternalModel(): Jugadores {
    return Jugadores(
        jugadorId = jugadorId,
        nombres = nombres,
        partidas = partidas
    )
}

// Dominio -> Entity
fun Jugadores.toEntity(): JugadorEntity {
    return JugadorEntity(
        jugadorId = jugadorId ?: 0,
        nombres = nombres,
        partidas = partidas
    )
}



// Entity -> Dominio
fun PartidaEntity.asExternalModel(): Partida {
    return Partida(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )
}

// Dominio -> Entity
fun Partida.toEntity(): PartidaEntity {
    return PartidaEntity(
        partidaId = partidaId,
        fecha = fecha,
        jugador1Id = jugador1Id,
        jugador2Id = jugador2Id,
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )
}