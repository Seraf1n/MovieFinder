package ru.seraf1n.moviefinder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.seraf1n.moviefinder.data.dao.FilmDao
import ru.seraf1n.moviefinder.data.entity.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}