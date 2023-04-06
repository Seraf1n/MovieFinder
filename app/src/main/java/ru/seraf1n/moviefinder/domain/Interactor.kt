package ru.seraf1n.moviefinder.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.seraf1n.moviefinder.data.API
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.data.settings.PreferenceProvider
import ru.seraf1n.moviefinder.utils.Converter

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: ru.seraf1n.remote_module.TmdbApi,
    private val preferences: PreferenceProvider

) {

    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    fun getFilmsFromApi(page: Int) {
        progressBarState.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page)
            .subscribeOn(Schedulers.io())
            .map {
                Converter.convertApiListToDtoList(it.tmdbFilms)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                },
                onNext = {
                    progressBarState.onNext(false)
                    repo.putToDb(it)
                }
            )

    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()

    fun getSearchResultFromApi(search: String): Observable<List<Film>> = retrofitService.getFilmFromSearch(API.KEY, "ru-RU", search, 1)
        .map {
            Converter.convertApiListToDtoList(it.tmdbFilms)
        }

    //метод для очистки списка
    fun clearListAfterCategoryChange() {
        Completable.fromSingle<List<Film>> {
            repo.clearDb()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}
