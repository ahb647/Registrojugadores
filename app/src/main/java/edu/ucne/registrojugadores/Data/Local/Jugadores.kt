package edu.ucne.registrojugadores.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity

@Database(
    entities = [JugadorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Jugadores : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao
}