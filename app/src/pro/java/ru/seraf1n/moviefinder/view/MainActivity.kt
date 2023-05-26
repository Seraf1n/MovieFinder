package ru.seraf1n.moviefinder.view

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*
import ru.seraf1n.moviefinder.App
import ru.seraf1n.moviefinder.R
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.databinding.ActivityMainBinding
import ru.seraf1n.moviefinder.utils.ConnectionChecker
import ru.seraf1n.moviefinder.view.fragments.*

private const val ANIM_DURATION = 1500L

private const val ALPHA_CHANEL = 1f

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMenuButtons()
        changeFragment(HomeFragment(), "home")

        initReceiver()

        initPromo()

    }

    private fun initPromo() {
        if (!App.instance.isPromoShown) {
            //Получаем доступ к Remote Config
            val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            //Устанавливаем настройки
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            //Вызываем метод, который получит данные с сервера и вешаем слушатель
            firebaseRemoteConfig.fetch()
                .addOnCompleteListener {
                    //Если все получилось успешно
                    if (it.isSuccessful) {
                        //активируем последний полученный конфиг с сервера
                        firebaseRemoteConfig.activate()
                        //Получаем ссылку
                        val filmLink = firebaseRemoteConfig.getString("film_link")
                        //Если поле не пустое
                        if (filmLink.isNotBlank
                                ()) {
                            //Ставим флаг, что уже промо показали
                            App.instance.isPromoShown = true
                            //Включаем промо верстку
                            binding.promoViewGroup.apply {
                                //Делаем видимой
                                visibility = View.VISIBLE
                                //Анимируем появление
                                animate()
                                    .setDuration(ANIM_DURATION)
                                    .alpha(ALPHA_CHANEL)
                                    .start()
                                //Вызываем метод, который загрузит постер в ImageView
                                setLinkForPoster(filmLink)
                                //Кнопка, по нажатии на которую промо уберется (желательно сделать отдельную кнопку с крестиком)
                                watchButton.setOnClickListener {
                                    visibility = View.GONE
                                }
                            }
                        }
                    }
                }
        }
    }


    private fun initReceiver() {
        receiver = ConnectionChecker()
        val filters = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        registerReceiver(receiver, filters)
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
                binding.fab.visibility = View.VISIBLE
                binding.fab.scaleX = 0 - slideOffset
                binding.fab.scaleY = 0 - slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
    }

    @Deprecated("Deprecated in Java")
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
        binding.topAppBar.setOnMenuItemClickListener {
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

        binding.fab.setOnClickListener {
            // val bottomSheetBehavior = BottomSheetBehavior.from(bottom_navigation_menu)
            (bottomSheetBehavior as BottomSheetBehavior).state = BottomSheetBehavior.STATE_EXPANDED
            it.visibility = View.GONE
        }

        binding.bottomNavigationMenu.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment(fragment ?: HomeFragment(), tag)
                    true
                }
                R.id.favorities -> {
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
                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SettingsFragment(), tag)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

