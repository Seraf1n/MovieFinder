package ru.seraf1n.moviefinder.di

import dagger.Component
import ru.seraf1n.moviefinder.di.modules.DatabaseModule
import ru.seraf1n.moviefinder.di.modules.DomainModule
import ru.seraf1n.moviefinder.viewmodel.HomeFragmentViewModel
import ru.seraf1n.moviefinder.viewmodel.SettingsFragmentViewModel
import ru.seraf1n.remote_module.RemoteProvider
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    dependencies = [RemoteProvider::class],
    modules = [
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    //метод для того, чтобы появилась возможность внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}