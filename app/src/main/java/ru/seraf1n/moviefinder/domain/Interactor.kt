package ru.seraf1n.moviefinder.domain

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.seraf1n.moviefinder.data.API
import ru.seraf1n.moviefinder.data.TmdbApi
import ru.seraf1n.moviefinder.data.entity.TmdbResultsDTO
import ru.seraf1n.moviefinder.utils.Converter
import ru.seraf1n.moviefinder.viewmodel.HomeFragmentViewModel

class Interactor(private val retrofitService: TmdbApi) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)

    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO> {
            override fun onResponse(
                call: Call<TmdbResultsDTO>,
                response: Response<TmdbResultsDTO>
            ) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

}
