package ru.seraf1n.moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.databinding.FragmentFavoritiesBinding
import ru.seraf1n.moviefinder.utils.AnimationHelper
import ru.seraf1n.moviefinder.view.MainActivity
import ru.seraf1n.moviefinder.view.rv_adapters.FilmListRecyclerAdapter

class FavoritesFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private var _binding: FragmentFavoritiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Получаем список при транзакции фрагмента
        val favoritesList: List<Film> = emptyList()

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentFavoritiesRoot,
            requireActivity(),
            1
        )

        binding.favoritesRecycler
            .apply {
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
        filmsAdapter.addItems(favoritesList)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}