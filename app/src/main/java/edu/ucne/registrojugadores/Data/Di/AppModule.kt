package edu.ucne.registrojugadores.Data.Di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadores.Data.Local.Jugadores
import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.Repository.JugadorRepositoryImpl
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJugadorDatabase(@ApplicationContext context: Context): Jugadores {
        return Room.databaseBuilder(
            context,
            Jugadores::class.java,
            "jugadoresDb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideJugadorDao(database: Jugadores): JugadorDao {
        return database.jugadorDao()
    }

    @Provides
    @Singleton
    fun provideJugadorRepository(dao: JugadorDao): JugadoresRepository {
        return JugadorRepositoryImpl(dao)
    }
}