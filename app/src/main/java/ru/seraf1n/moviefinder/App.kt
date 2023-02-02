package ru.seraf1n.moviefinder

import android.app.Application
import ru.seraf1n.moviefinder.di.AppComponent
import ru.seraf1n.moviefinder.di.DaggerAppComponent

class App : Application() {

    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}