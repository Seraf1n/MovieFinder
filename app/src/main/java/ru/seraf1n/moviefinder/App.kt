package ru.seraf1n.moviefinder
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import ru.seraf1n.moviefinder.di.AppComponent
import ru.seraf1n.moviefinder.di.DaggerAppComponent
import ru.seraf1n.moviefinder.di.modules.DatabaseModule
import ru.seraf1n.moviefinder.di.modules.DomainModule
import ru.seraf1n.moviefinder.view.notifications.NotificationConstants.CHANNEL_ID
import ru.seraf1n.remote_module.DaggerRemoteComponent

class App : Application() {

    lateinit var dagger: AppComponent
    var isPromoShown = false
    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Задаем имя, описание и важность канала
            val name = "WatchLaterChannel"
            val descriptionText = "FilmsSearch notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            //Создаем канал, передав в параметры его ID(строка), имя(строка), важность(константа)
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            //Отдельно задаем описание
            mChannel.description = descriptionText
            //Получаем доступ к менеджеру нотификаций
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //Регистрируем канал
            notificationManager.createNotificationChannel(mChannel)
        }

    }
    companion object {
        lateinit var instance: App
            private set
    }
}