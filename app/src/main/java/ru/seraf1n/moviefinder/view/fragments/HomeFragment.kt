package ru.seraf1n.moviefinder.view.fragments

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.seraf1n.moviefinder.utils.AnimationHelper
import ru.seraf1n.moviefinder.view.rv_adapters.FilmListRecyclerAdapter
import ru.seraf1n.moviefinder.view.MainActivity
import ru.seraf1n.moviefinder.databinding.FragmentHomeBinding
import ru.seraf1n.moviefinder.domain.Film
import ru.seraf1n.moviefinder.viewmodel.HomeFragmentViewModel
import java.util.*

class HomeFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.homeFragmentRoot,
            requireActivity(),
            1
        )
        initRecyclerView()

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        viewModel.filmsListLiveData.observe(viewLifecycleOwner) {
            filmsDataBase = it
        }

        //Подключаем слушателя изменений введенного текста в поиска
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    filmsAdapter.addItems(filmsDataBase)
                    return true
                }
                //Фильтруем список на поискк подходящих сочетаний
                val result = filmsDataBase.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.addItems(result)
                return true
            }
        })
    }

    private fun initRecyclerView() {
        //находим наш RV
        binding.mainRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания
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
            val decorator = TopSpacingItemDecoration(8)
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
}

class TopSpacingItemDecoration(private val paddingInDp: Int) : RecyclerView.ItemDecoration() {
    private val Int.convertPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = paddingInDp.convertPx
        outRect.right = paddingInDp.convertPx
        outRect.left = paddingInDp.convertPx

    }
}