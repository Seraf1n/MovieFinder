package ru.seraf1n.moviefinder.di.modules

import dagger.Module
import dagger.Provides
import ru.seraf1n.moviefinder.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}