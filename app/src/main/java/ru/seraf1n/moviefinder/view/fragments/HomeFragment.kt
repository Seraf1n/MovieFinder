package ru.seraf1n.moviefinder.view.fragments

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.databinding.FragmentHomeBinding
import ru.seraf1n.moviefinder.utils.AnimationHelper
import ru.seraf1n.moviefinder.utils.AutoDisposable
import ru.seraf1n.moviefinder.utils.addTo
import ru.seraf1n.moviefinder.view.MainActivity
import ru.seraf1n.moviefinder.view.rv_adapters.FilmListRecyclerAdapter
import ru.seraf1n.moviefinder.viewmodel.HomeFragmentViewModel
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private val autoDisposable = AutoDisposable()
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            filmsAdapter.addItems(field)
        }

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDisposable.bindTo(lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initPullToRefresh()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.homeFragmentRoot, requireActivity(), POSITION_UNO
        )
        initRecyclerView()
        //Подключаем слушателя изменений введенного текста в поиска
        initSearchView()

        //Кладем нашу БД в RV
        viewModel.filmsListLiveData.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { list ->
                filmsAdapter.addItems(list)
                filmsDataBase = list
            }
            .addTo(autoDisposable)



        viewModel.showProgressBar.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                binding.progressBar.isVisible = it
            }
            .addTo(autoDisposable)
    }

    private fun initSearchView() {
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        Observable.create(){ subscriber ->
            //Вешаем слушатель на клавиатуру
            binding.searchView.setOnQueryTextListener(object :
            //Вызывается на ввод символов
                SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    filmsAdapter.items.clear()
                    subscriber.onNext(newText)
                    return false
                }
                //Вызывается по нажатию кнопки "Поиск"
                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            })
        }
            .subscribeOn(Schedulers.io())
            .map {
                it.lowercase(Locale.getDefault()).trim()
            }
            .debounce(TIMEOUT_800, TimeUnit.MILLISECONDS)
            .filter {
                //Если в поиске пустое поле, возвращаем список фильмов по умолчанию
                viewModel.getFilms()
                it.isNotBlank()
            }
            .flatMap {
                viewModel.getSearchResult(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
            onError = {
                Toast.makeText(requireContext(), ERROR, Toast.LENGTH_SHORT).show()
            },
            onNext = {
                filmsAdapter.addItems(it)
            }
        )
            .addTo(autoDisposable)
    }


    private fun initRecyclerView() {
        //находим наш RV
        binding.mainRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })

            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())

            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(PADDING_8)
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(initMovieBase())
    }

    private fun initMovieBase(): List<Film> {
        return filmsDataBase
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun initPullToRefresh() {
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.items.clear()
            //Делаем новый запрос фильмов на сервер
            viewModel.getFilms()
            //Убираем крутящееся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }

   companion object const{
       private const val POSITION_UNO = 1
       private const val PADDING_8 = 8
       private const val TIMEOUT_800 = 800L
       private const val ERROR = "Что-то пошло не так"
    }
}

class TopSpacingItemDecoration(private val paddingInDp: Int) : RecyclerView.ItemDecoration() {
    private val Int.convertPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = paddingInDp.convertPx
        outRect.right = paddingInDp.convertPx
        outRect.left = paddingInDp.convertPx

    }
}

