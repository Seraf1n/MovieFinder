package ru.seraf1n.moviefinder.data

import kotlinx.coroutines.flow.Flow
import ru.seraf1n.moviefinder.data.dao.FilmDao
import ru.seraf1n.moviefinder.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): Flow<List<Film>> = filmDao.getCachedFilms()
}