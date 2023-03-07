package ru.seraf1n.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.seraf1n.moviefinder.App
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.domain.Interactor
import java.util.concurrent.Executors
import javax.inject.Inject

class HomeFragmentViewModel constructor(page: Int = 1) : ViewModel() {

    val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    @Inject
    lateinit var interactor: Interactor
    val filmsListLiveData: LiveData<List<Film>>

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = interactor.getFilmsFromDB()
        getFilms()
    }
    fun getFilms() {
        interactor.getFilmsFromApi(1, object : ApiCallback {
            override fun onSuccess() {
                showProgressBar.postValue(false)
            }

            override fun onFailure() {
                Executors.newSingleThreadExecutor().execute {
                    showProgressBar.postValue(false)
                }
            }
        })
    }

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }

}