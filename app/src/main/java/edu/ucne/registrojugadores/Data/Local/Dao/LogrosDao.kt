package edu.ucne.registrojugadores.Data.Local.Dao

import androidx.room.*
import edu.ucne.registrojugadores.Data.Local.Entity.LogrosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogrosDao {

    // Obtener todos los logros ordenados por ID descendente
    @Query("SELECT * FROM Logros ORDER BY logroId DESC")
    fun observedAll(): Flow<List<LogrosEntity>>

    // Obtener un logro por ID
    @Query("SELECT * FROM Logros WHERE logroId = :id")
    suspend fun getById(id: Int): LogrosEntity?

    // Insertar o actualizar automáticamente
    @Upsert
    suspend fun upsert(entity: LogrosEntity)

    // Eliminar un logro
    @Delete
    suspend fun delete(entity: LogrosEntity)

    // Eliminar un logro por ID
    @Query("DELETE FROM Logros WHERE logroId = :id")
    suspend fun deleteById(id: Int)
}
