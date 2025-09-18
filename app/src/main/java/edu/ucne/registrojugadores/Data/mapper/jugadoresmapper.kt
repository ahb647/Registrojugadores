package edu.ucne.registrojugadores.Data.mapper

import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import edu.ucne.registrojugadores.Data.Local.Entity.PartidaEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Model.Partida

// -------------------- JUGADORES --------------------

// Entity -> Dominio
fun JugadorEntity.asExternalModel(): Jugadores {
    return Jugadores(
        jugadorId = jugadorId, // int no nullable en Entity, int? en dominio
        nombres = nombres,
        partidas = partidas
    )
}

// Dominio -> Entity
fun Jugadores.toEntity(): JugadorEntity {
    return JugadorEntity(
        jugadorId = jugadorId ?: 0, // si es null, Room asignará ID automáticamente
        nombres = nombres,
        partidas = partidas
    )
}

// -------------------- PARTIDAS --------------------

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
        partidaId = partidaId ?: 0,          // si es null, Room lo reemplaza
        fecha = fecha,
        jugador1Id = jugador1Id ?: 0,        // forzamos Int
        jugador2Id = jugador2Id ?: 0,        // forzamos Int
        ganadorId = ganadorId,
        esFinalizada = esFinalizada
    )
}
