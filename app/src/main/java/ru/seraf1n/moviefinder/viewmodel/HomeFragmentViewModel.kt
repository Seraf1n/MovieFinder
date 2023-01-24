package ru.seraf1n.moviefinder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.seraf1n.moviefinder.domain.Film
import ru.seraf1n.moviefinder.domain.Interactor

class HomeFragmentViewModel constructor(page:Int = 1) : ViewModel(), KoinComponent {
    val filmsListLiveData:  MutableLiveData<List<Film>> = MutableLiveData()
    //Инициализируем интерактор
    private val interactor: Interactor by inject()


    init {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

}