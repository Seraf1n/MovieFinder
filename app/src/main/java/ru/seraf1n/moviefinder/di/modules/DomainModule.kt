package ru.seraf1n.moviefinder.di.modules

import dagger.Module
import dagger.Provides
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.TmdbApi
import ru.seraf1n.moviefinder.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi) = Interactor(repo = repository, retrofitService = tmdbApi)
}