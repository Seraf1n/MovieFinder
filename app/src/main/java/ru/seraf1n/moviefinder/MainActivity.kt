package ru.seraf1n.moviefinder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Запускаем фрагмент при старте
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null)
            .commit()

        initMenuButtons()
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_navigation_menu)
        (bottomSheetBehavior as BottomSheetBehavior).state = BottomSheetBehavior.STATE_HIDDEN
        initCallBacks()
    }

    private fun initCallBacks() {
        (bottomSheetBehavior as BottomSheetBehavior).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fab.scaleX = 0 - slideOffset
                fab.scaleY = 0 - slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.Exit_msg), Toast.LENGTH_SHORT).show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }

    }

    companion object {
        const val TIME_INTERVAL = 2000
    }

    fun launchDetailsFragment(film: Film) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initMenuButtons() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "${it.title}", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nightMode -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    true
                }

                else -> false
            }
        }

        fab.setOnClickListener {
            // val bottomSheetBehavior = BottomSheetBehavior.from(bottom_navigation_menu)
            (bottomSheetBehavior as BottomSheetBehavior).state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottom_navigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    Snackbar.make(
                        this,
                        findViewById(R.id.fab),
                        "${it.title}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.watch_later -> {
                    Snackbar.make(
                        this,
                        findViewById(R.id.bottom_navigation),
                        "${it.title}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this, "${it.title}", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}

