package ru.seraf1n.moviefinder.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.seraf1n.moviefinder.App
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.domain.Interactor
import javax.inject.Inject

const val DEFAULT_PAGE = 1

class HomeFragmentViewModel constructor(page: Int = 1) : ViewModel() {



    @Inject
    lateinit var interactor: Interactor
    val filmsListLiveData: Observable<List<Film>>
    val showProgressBar: BehaviorSubject<Boolean>

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