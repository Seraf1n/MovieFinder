package ru.seraf1n.moviefinder.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Observable
import ru.seraf1n.moviefinder.data.entity.Film


@Dao
interface FilmDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): Observable<List<Film>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)
}