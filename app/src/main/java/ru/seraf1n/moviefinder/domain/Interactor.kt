package ru.seraf1n.moviefinder.domain

import ru.seraf1n.moviefinder.data.MainRepository

class Interactor(val repo: MainRepository) {
    fun getFilmsDB(): List<Film> = repo.filmsDataBase
}