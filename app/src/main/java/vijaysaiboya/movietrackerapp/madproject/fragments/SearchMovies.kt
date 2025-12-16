package vijaysaiboya.movietrackerapp.madproject.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun SearchMoviesScreen(navController: NavHostController) {

    var search by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var filteredMovies by remember { mutableStateOf(listOf<Movie>()) }
    var loading by remember { mutableStateOf(true) }

    val genres = listOf("All", "Action", "Comedy", "Drama", "Crime", "Romance", "Sci-Fi", "Thriller", "Other")

    val fastKeywords = listOf("movie", "best", "new")

    LaunchedEffect(Unit) {
        loading = true

        val results = coroutineScope {
            fastKeywords.map { key ->
                async { fetchMoviesFromOMDb(key) }
            }.awaitAll().flatten()
        }

        // Add FAST genre prediction
        val withGenre = results
            .distinctBy { it.imdbId }
            .take(100)
            .map { it.copy(genre = guessGenre(it.title)) }

        movies = withGenre
        filteredMovies = withGenre

        loading = false
    }

    // Search movies
    LaunchedEffect(search) {
        if (search.length >= 3) {
            loading = true

            val results = fetchMoviesFromOMDb(search)
            val withGenre = results.map { it.copy(genre = guessGenre(it.title)) }

            movies = withGenre
            filteredMovies = withGenre
            loading = false
        } else {
            filteredMovies = movies
        }
    }

    // Apply Genre Filter
    LaunchedEffect(selectedGenre) {

        if (selectedGenre == "All") {
            filteredMovies = movies
            return@LaunchedEffect
        }

        loading = true

        // Get keywords for selected genre
        val keywords = genreKeywords(selectedGenre)

        // Parallel OMDb calls
        val results = coroutineScope {
            keywords.map { key ->
                async { fetchMoviesFromOMDb(key) }
            }.awaitAll().flatten()
        }

        filteredMovies = results.distinctBy { it.imdbId }

        loading = false
    }



    // ================================
    // UI
    // ================================
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

        // Genre Filter Chips
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
                            .clickable {
                                navController.navigate("details/${movie.imdbId}")
                            }
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

fun guessGenre(title: String): String {
    val t = title.lowercase()

    return when {
        listOf("war", "man", "battle", "fight", "hero", "dark").any { it in t } -> "Action"
        listOf("love", "heart", "romance").any { it in t } -> "Romance"
        listOf("king", "queen", "drama", "life").any { it in t } -> "Drama"
        listOf("funny", "comedy", "laugh").any { it in t } -> "Comedy"
        listOf("crime", "detective", "murder").any { it in t } -> "Crime"
        listOf("future", "space", "alien", "sci-fi", "robot").any { it in t } -> "Sci-Fi"
        listOf("thrill", "secret", "fear", "dark").any { it in t } -> "Thriller"
        else -> "Other"
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

fun genreKeywords(genre: String): List<String> {
    return when (genre) {
        "Action" -> listOf("action", "fight", "war", "hero")
        "Romance" -> listOf("romance", "love", "heart", "kiss")
        "Drama" -> listOf("drama", "family", "life", "story")
        "Comedy" -> listOf("comedy", "funny", "laugh", "humor")
        "Crime" -> listOf("crime", "detective", "murder", "case")
        "Sci-Fi" -> listOf("scifi", "space", "future", "alien")
        "Thriller" -> listOf("thriller", "fear", "mystery", "dark")
        else -> listOf("movie", "best")
    }
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

