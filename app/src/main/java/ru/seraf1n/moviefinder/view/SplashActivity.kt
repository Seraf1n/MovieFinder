package ru.seraf1n.moviefinder.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.seraf1n.moviefinder.R

const val STANDART_DELAY = 3000L

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        CoroutineScope(Dispatchers.Main).launch {

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            delay(STANDART_DELAY)
            startActivity(intent)
            finish()

        }


    }
}