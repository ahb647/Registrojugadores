package edu.ucne.registrojugadores.Data.mapper

import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import edu.ucne.registrojugadores.Data.Local.Entity.PartidaEntity
import edu.ucne.registrojugadores.Data.Local.Entity.LogrosEntity
import edu.ucne.registrojugadores.Domain.Model.Model.Jugadores
import edu.ucne.registrojugadores.Domain.Model.Model.Partida
import edu.ucne.registrojugadores.Domain.Model.Model.Logros

// -------------------- JUGADORES --------------------
fun JugadorEntity.asExternalModel(): Jugadores = Jugadores(
    jugadorId = jugadorId,
    nombres = nombres,
    partidas = partidas
)

fun Jugadores.toEntity(): JugadorEntity = JugadorEntity(
    jugadorId = jugadorId ?: 0,
    nombres = nombres,
    partidas = partidas
)

// -------------------- PARTIDAS --------------------
fun PartidaEntity.asExternalModel(): Partida = Partida(
    partidaId = partidaId,
    fecha = fecha,
    jugador1Id = jugador1Id,
    jugador2Id = jugador2Id,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)

fun Partida.toEntity(): PartidaEntity = PartidaEntity(
    partidaId = partidaId ?: 0,
    fecha = fecha,
    jugador1Id = jugador1Id ?: 0,
    jugador2Id = jugador2Id ?: 0,
    ganadorId = ganadorId,
    esFinalizada = esFinalizada
)

// -------------------- LOGROS --------------------
fun LogrosEntity.asExternalModel(): Logros = Logros(
    logroId = logroId,
    logroNombre = logroNombre,
    descripcion = descripcion
)

fun Logros.toEntity(): LogrosEntity = LogrosEntity(
    logroId = logroId ?: 0,
    logroNombre = logroNombre,
    descripcion = descripcion
)
