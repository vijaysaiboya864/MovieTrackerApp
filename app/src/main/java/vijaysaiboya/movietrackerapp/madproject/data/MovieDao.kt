package vijaysaiboya.movietrackerapp.madproject.data


import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE category = :category")
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    @Query("DELETE FROM movies WHERE imdbId = :id")
    suspend fun deleteMovie(id: String)
}
