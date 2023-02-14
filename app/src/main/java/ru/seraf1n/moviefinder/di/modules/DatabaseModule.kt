package ru.seraf1n.moviefinder.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.seraf1n.moviefinder.data.MainRepository
import ru.seraf1n.moviefinder.data.db.DatabaseHelper
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}