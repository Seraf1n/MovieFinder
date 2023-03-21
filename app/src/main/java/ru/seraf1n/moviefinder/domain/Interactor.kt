package ru.seraf1n.moviefinder.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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

    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var progressBarState = Channel<Boolean>(Channel.CONFLATED)
    fun getFilmsFromApi(page: Int) {
        scope.launch {
            progressBarState.send(true)
        }
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
                    scope.launch {
                        repo.putToDb(list)
                        progressBarState.send(false)
                    }

                }

                override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                    //В случае провала вызываем другой метод коллбека
                    scope.launch {
                        progressBarState.send(false)
                    }

                }
            })
    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFromDB()
}
