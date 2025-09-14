package edu.ucne.registrojugadores.Data.Di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrojugadores.Data.Local.AppDatabase
import edu.ucne.registrojugadores.Data.Local.Dao.JugadorDao
import edu.ucne.registrojugadores.Data.Local.Dao.PartidaDao
import edu.ucne.registrojugadores.Data.Repository.JugadorRepositoryImpl
import edu.ucne.registrojugadores.Data.Repository.PartidasRepositoryImpl
import edu.ucne.registrojugadores.Domain.Model.Repository.JugadoresRepository
import edu.ucne.registrojugadores.Domain.Model.Repository.PartidasRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // ---------------------------
    // Jugadores
    // ---------------------------
    @Provides
    @Singleton
    fun provideJugadorDao(database: AppDatabase): JugadorDao {
        return database.jugadorDao()
    }

    @Provides
    @Singleton
    fun provideJugadorRepository(dao: JugadorDao): JugadoresRepository {
        return JugadorRepositoryImpl(dao)
    }

    // ---------------------------
    // Partidas
    // ---------------------------
    @Provides
    @Singleton
    fun providePartidaDao(database: AppDatabase): PartidaDao {
        return database.partidaDao()
    }

    @Provides
    @Singleton
    fun providePartidasRepository(dao: PartidaDao): PartidasRepository {
        return PartidasRepositoryImpl(dao)
    }
}
