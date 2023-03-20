package ru.seraf1n.moviefinder.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import ru.seraf1n.moviefinder.App
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.domain.Interactor
import javax.inject.Inject

const val DEFAULT_PAGE = 1

class HomeFragmentViewModel constructor(page: Int = 1) : ViewModel() {

    val showProgressBar: Channel<Boolean>

    @Inject
    lateinit var interactor: Interactor
    val filmsListLiveData: Flow<List<Film>>

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        filmsListLiveData = interactor.getFilmsFromDB()
        getFilms()
    }

    fun getFilms() {
        interactor.getFilmsFromApi(DEFAULT_PAGE)
    }

}