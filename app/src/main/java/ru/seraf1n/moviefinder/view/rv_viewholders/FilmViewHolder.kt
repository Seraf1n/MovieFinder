package ru.seraf1n.moviefinder.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.seraf1n.moviefinder.data.entity.Film
import ru.seraf1n.moviefinder.databinding.FilmItemBinding
import ru.seraf1n.remote_module.entity.ApiConstants

const val RATING_INDEX = 10
private const val POSTER_PV = "w342"

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val filmItemBinding = FilmItemBinding.bind(itemView)
    //Привязываем view из layout к переменным
    private val title = filmItemBinding.title
    private val poster = filmItemBinding.poster
    private val description = filmItemBinding.description
    //Вот здесь мы находим в верстке наш прогресс бар для рейтинга
    private val ratingDonut = filmItemBinding.ratingDonut


    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film) {
        //Устанавливаем заголовок
        title.text = film.title
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(ApiConstants.IMAGES_URL + POSTER_PV + film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(poster)
        //Устанавливаем описание
        description.text = film.description
        //Устанавливаем рэйтинг
        ratingDonut.setProgress((film.rating * RATING_INDEX).toInt())
    }
}