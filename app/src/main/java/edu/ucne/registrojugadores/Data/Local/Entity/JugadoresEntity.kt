package edu.ucne.registrojugadores.Data.Local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Jugadores")
data class JugadorEntity(
    @PrimaryKey(autoGenerate = false)
    val jugadorId: Int,
    val nombres: String,
    val email: String
)