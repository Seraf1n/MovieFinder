package ru.seraf1n.moviefinder.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.film_item.view.*
import ru.seraf1n.moviefinder.data.ApiConstants
import ru.seraf1n.moviefinder.domain.Film

const val RATING_INDEX = 10
private const val POSTER_PV = "w342"

//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    //Привязываем View из layout к переменным
    private val title = itemView.title
    private val poster = itemView.poster
    private val ratingDonut = itemView.rating_donut
    private val description = itemView.description


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