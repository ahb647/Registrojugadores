package edu.ucne.registrojugadores.Data.mapper

import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores

// Convertir Entity -> Dominio
fun JugadorEntity.asExternalModel(): Jugadores {
    return Jugadores(
        jugadorId = jugadorId,
        nombres = nombres,
        partidas = partidas
    )
}

// Convertir Dominio -> Entity
fun Jugadores.toEntity(): JugadorEntity {
    return JugadorEntity(
        jugadorId = jugadorId ?: 0,
        nombres = nombres,
        partidas = partidas
    )
}