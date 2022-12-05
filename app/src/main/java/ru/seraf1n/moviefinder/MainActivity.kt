package ru.seraf1n.moviefinder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        initMenuButtons()
        changeFragment(HomeFragment(), "home")
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

        if (!supportFragmentManager.fragments.last().tag.equals("details")) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {

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
        val tag = "details"
        val fragment = checkFragmentExistence(tag) ?: DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle
        changeFragment(fragment, tag)

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
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment(fragment ?: HomeFragment(), tag)
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: FavoritesFragment(), tag)
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: WatchLaterFragment(), tag)
                    true
                }
                R.id.selections -> {
                    val tag = "selections"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SelectionsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

}

