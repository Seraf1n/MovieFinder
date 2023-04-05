package ru.seraf1n.moviefinder.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.TmdbApi
import ru.seraf1n.moviefinder.data.settings.PreferenceProvider
import ru.seraf1n.moviefinder.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule(val context: Context)  {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(
        repository: MainRepository,
        tmdbApi: TmdbApi,
        preferenceProvider: PreferenceProvider
    ) = Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}