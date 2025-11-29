package vijaysaiboya.movietrackerapp.madproject.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun SearchMoviesScreen() {

    var search by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var filteredMovies by remember { mutableStateOf(listOf<Movie>()) }
    var loading by remember { mutableStateOf(true) }

    val genres = listOf("All", "Action", "Comedy", "Drama", "Crime", "Sci-Fi", "Romance", "Thriller")

    LaunchedEffect(Unit) {
        loading = true
        val initialList = fetchInitialMovies()

        // Get genres for each movie
        val moviesWithGenre = initialList.map { movie ->
            val genre = fetchMovieGenre(movie.imdbId)
            movie.copy(genre = genre)
        }

        movies = moviesWithGenre
        filteredMovies = moviesWithGenre
        loading = false
    }

    LaunchedEffect(search) {
        if (search.length >= 3) {
            loading = true
            val results = fetchMoviesFromOMDb(search)

            // fetch genre for each result
            val listWithGenre = results.map { movie ->
                val genre = fetchMovieGenre(movie.imdbId)
                movie.copy(genre = genre)
            }

            movies = listWithGenre
            loading = false
        }
    }

    LaunchedEffect(selectedGenre, movies) {
        filteredMovies = if (selectedGenre == "All") {
            movies
        } else {
            movies.filter { it.genre.contains(selectedGenre, ignoreCase = true) }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search movies...") },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = PrimaryBlue,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(genres) { g ->
                FilterChip(
                    selected = selectedGenre == g,
                    onClick = { selectedGenre = g },
                    label = { Text(g) },
                    colors = FilterChipDefaults.filterChipColors(
                        labelColor = Color.White,
                        selectedContainerColor = PrimaryBlue,
                        containerColor = Color.DarkGray
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(filteredMovies) { movie ->

                    Column(
                        modifier = Modifier
                            .background(Color(0xFF26252B), RoundedCornerShape(12.dp))
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(movie.poster),
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            movie.title,
                            color = Color.White,
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


suspend fun fetchInitialMovies(): List<Movie> {

    val keywords = listOf(
        "avengers", "batman", "spider", "king", "girl",
        "star", "love", "war", "man", "world"
    )

    val results = mutableListOf<Movie>()

    for (key in keywords) {
        results += fetchMoviesFromOMDb(key)
    }

    return results.distinctBy { it.imdbId }.take(100)
}


suspend fun fetchMoviesFromOMDb(query: String): List<Movie> {
    val url = "https://www.omdbapi.com/?apikey=$OMDB_KEY&s=$query&type=movie"

    return withContext(Dispatchers.IO) {
        val list = mutableListOf<Movie>()
        val conn = URL(url).openConnection() as HttpURLConnection

        try {
            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)

            if (!json.has("Search")) return@withContext emptyList()

            val results = json.getJSONArray("Search")

            for (i in 0 until results.length()) {
                val obj = results.getJSONObject(i)

                list.add(
                    Movie(
                        title = obj.getString("Title"),
                        poster = obj.getString("Poster"),
                        imdbId = obj.getString("imdbID")
                    )
                )
            }
        } finally {
            conn.disconnect()
        }

        list
    }
}

suspend fun fetchMovieGenre(imdbId: String): String {
    val url = "https://www.omdbapi.com/?apikey=$OMDB_KEY&i=$imdbId"

    return withContext(Dispatchers.IO) {
        val conn = URL(url).openConnection() as HttpURLConnection

        try {
            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)

            json.optString("Genre", "")
        } finally {
            conn.disconnect()
        }
    }
}

