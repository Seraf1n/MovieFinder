package ru.seraf1n.moviefinder.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.seraf1n.moviefinder.data.API
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.TmdbApi
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.data.entity.TmdbResultsDTO
import ru.seraf1n.moviefinder.data.settings.PreferenceProvider
import ru.seraf1n.moviefinder.utils.Converter

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider

) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    fun getFilmsFromApi(page: Int) {
        progressBarState.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page)
            .enqueue(object : Callback<TmdbResultsDTO> {
                override fun onResponse(
                    call: Call<TmdbResultsDTO>,
                    response: Response<TmdbResultsDTO>
                ) {
                    //При успехе мы вызываем метод, передаем onSuccess и в этот коллбэк список фильмов
                    val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
                    //Кладем фильмы в бд
                    Completable.fromSingle<List<Film>> {
                        repo.putToDb(list)
                    }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    progressBarState.onNext(false)
                }

                override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                    progressBarState.onNext(false)
                }
            })
    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()
}
