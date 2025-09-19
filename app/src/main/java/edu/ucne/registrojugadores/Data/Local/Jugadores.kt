package edu.ucne.registrojugadores.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.Local.Dao.PartidaDao
import edu.ucne.registrojugadores.Data.Local.Dao.LogrosDao
import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import edu.ucne.registrojugadores.Data.Local.Entity.PartidaEntity
import edu.ucne.registrojugadores.Data.Local.Entity.LogrosEntity

@Database(
    entities = [JugadorEntity::class, PartidaEntity::class, LogrosEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun jugadorDao(): JugadorDao
    abstract fun partidaDao(): PartidaDao
    abstract fun logrosDao(): LogrosDao
}
