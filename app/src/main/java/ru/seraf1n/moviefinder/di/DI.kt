package ru.seraf1n.moviefinder.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.seraf1n.moviefinder.BuildConfig
import ru.seraf1n.moviefinder.data.ApiConstants
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.TmdbApi
import ru.seraf1n.moviefinder.domain.Interactor
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 30L
object DI {
    val mainModule = module {
        //Создаем репозиторий
        single { MainRepository() }
        //Создаем объект для получения данных из сети
        single<TmdbApi> {
            val okHttpClient = OkHttpClient.Builder()
                //Настраиваем таймауты для медленного интернета
                .callTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                //Добавляем логгер
                .addInterceptor(HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                })
                .build()
            //Создаем ретрофит
            val retrofit = Retrofit.Builder()
                //Указываем базовый URL из констант
                .baseUrl(ApiConstants.BASE_URL)
                //Добавляем конвертер
                .addConverterFactory(GsonConverterFactory.create())
                //Добавляем кастомный клиент
                .client(okHttpClient)
                .build()
            //Создаем сам сервис с методами для запросов
            retrofit.create(TmdbApi::class.java)
        }
        //Создаем интерактор
        single { Interactor(get(), get()) }
    }
}