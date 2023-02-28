package ru.seraf1n.moviefinder.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.seraf1n.moviefinder.R
import ru.seraf1n.moviefinder.data.ApiConstants
import ru.seraf1n.moviefinder.databinding.FragmentDetailsBinding
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.utils.AnimationHelper


private const val POSTER_DETAILED = "w780"

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentDetails,
            requireActivity(),
            1
        )
        initMovieDetails()
        initButton()
    }

    private fun initButton() {
        binding.detailsFabFavorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )

        binding.detailsFabFavorites.setOnClickListener {
            if (!film.isInFavorites) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                film.isInFavorites = true
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                film.isInFavorites = false
            }
        }
        binding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    private fun initMovieDetails() {
        film = arguments?.get("film") as Film

        //Устанавливаем заголовок
        binding.toolbar.title = film.title
        //Устанавливаем картинку
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + POSTER_DETAILED + film.poster)
            .centerCrop()
            .into(binding.detailsPoster)

        //Устанавливаем описание
        binding.detailsDescription.text = film.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}