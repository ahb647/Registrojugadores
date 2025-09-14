package edu.ucne.registrojugadores.Data.Local.Dao

import androidx.room.*
import edu.ucne.registrojugadores.Data.Local.Entity.PartidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {

    @Query("SELECT * FROM Partidas ORDER BY fecha DESC")
    fun getAllPartidas(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM Partidas WHERE partidaId = :id")
    suspend fun getPartidaById(id: Int): PartidaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartida(partida: PartidaEntity)

    @Delete
    suspend fun deletePartida(partida: PartidaEntity)

    @Update
    suspend fun updatePartida(partida: PartidaEntity)
}