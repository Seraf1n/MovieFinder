package ru.seraf1n.moviefinder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

private const val BATERY_LOW = "Батарея разряжена"

private const val POWER_CONNECTED = "Зарядка подключена"

class ConnectionChecker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        //Если интента нет, то выходим из метода
        if (intent == null) return
        //Проверяем, какой пришел action
        when (intent.action) {
            //Если пришел низкий заряд батарее
            Intent.ACTION_BATTERY_LOW -> Toast.makeText(context, BATERY_LOW, Toast.LENGTH_SHORT).show()
            //Если пришло подключение к зарядке
            Intent.ACTION_POWER_CONNECTED -> Toast.makeText(context, POWER_CONNECTED, Toast.LENGTH_SHORT).show()
        }

    }
}