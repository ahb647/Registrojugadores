package edu.ucne.registrojugadores.Data.Local.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logros")
data class LogrosEntity(
    @PrimaryKey(autoGenerate = true)
    val logroId: Int? = null,
    val logroNombre: String = "",
    val descripcion: String = ""
)