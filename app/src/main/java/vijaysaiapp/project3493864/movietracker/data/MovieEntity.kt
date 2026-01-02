package vijaysaiapp.project3493864.movietracker.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val poster: String,
    val plot: String,
    val genre: String,
    val released: String,
    val category: String
)
