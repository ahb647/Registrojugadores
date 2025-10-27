package edu.ucne.registrojugadores.Data.Local.Dao



import androidx.room.*
import edu.ucne.registrojugadores.Data.Local.Entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {

    @Query("SELECT * FROM Jugadores")
    fun getAllJugadores(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE jugadorId = :id")
    suspend fun getJugadorById(id: Int): JugadorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJugador(jugador: JugadorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJugadores(jugadores: List<JugadorEntity>)

    @Delete
    suspend fun deleteJugador(jugador: JugadorEntity)

    @Update
    suspend fun updateJugador(jugador: JugadorEntity)

    @Query("DELETE FROM Jugadores")
    suspend fun clearAll()
}