package ru.seraf1n.moviefinder.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.view.notifications.NotificationConstants
import ru.seraf1n.moviefinder.view.notifications.NotificationHelper

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val bundle = intent?.getBundleExtra(NotificationConstants.FILM_BUNDLE_KEY)
        val film: Film = bundle?.get(NotificationConstants.FILM_KEY) as Film

        NotificationHelper.createNotification(context!!, film)
    }
}