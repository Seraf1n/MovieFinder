package ru.seraf1n.moviefinder

import android.app.Application
import ru.seraf1n.moviefinder.di.AppComponent
import ru.seraf1n.moviefinder.di.DaggerAppComponent
import ru.seraf1n.moviefinder.di.modules.DatabaseModule
import ru.seraf1n.moviefinder.di.modules.DomainModule
import ru.seraf1n.moviefinder.di.modules.RemoteModule

class App : Application() {

    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }


    companion object {
        lateinit var instance: App
            private set
    }
}