package ru.seraf1n.moviefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMenuButtons()
    }

    private fun initMenuButtons() {
        findViewById<Button>(R.id.btnMenu).setOnClickListener {
            Toast.makeText(this, "${(it as MaterialButton).text}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnComp).setOnClickListener {
            Toast.makeText(this, "${(it as MaterialButton).text}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnFav).setOnClickListener {
            Toast.makeText(this, "${(it as MaterialButton).text}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnLater).setOnClickListener {
            Toast.makeText(this, "${(it as MaterialButton).text}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            Toast.makeText(this, "${(it as MaterialButton).text}", Toast.LENGTH_SHORT).show()
        }
    }
}