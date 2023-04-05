package ru.seraf1n.moviefinder.data.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import ru.seraf1n.moviefinder.data.dao.FilmDao

@Parcelize
@Entity(tableName = "cached_films", indices = [Index(value = ["title"], unique = true)])
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String?, //У нас будет приходить ссылка на картинку, так что теперь это String
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0, //Приходит нецелое число с API
    var isInFavorites: Boolean = false
) : Parcelable

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}