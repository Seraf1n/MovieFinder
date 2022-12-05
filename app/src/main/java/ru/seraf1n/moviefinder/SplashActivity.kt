package ru.seraf1n.moviefinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        CoroutineScope(Dispatchers.Main).launch {

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            delay(3000)
            startActivity(intent)
            finish()

        }


    }
}