package vijaysaiboya.movietrackerapp.madproject.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vijaysaiboya.movietrackerapp.madproject.data.MovieDao
import vijaysaiboya.movietrackerapp.madproject.data.MovieDatabase
import vijaysaiboya.movietrackerapp.madproject.data.MovieEntity
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun MovieDetailsScreen(
    imdbId: String,
    navController: NavHostController
) {
    var movie by remember { mutableStateOf<MovieDetails?>(null) }
    var loading by remember { mutableStateOf(true) }

    val dao = MovieDatabase.getDatabase(LocalContext.current).movieDao()
    val scope = rememberCoroutineScope()

    // Track saved state
    var isWatchLater by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }

    // Load movie + saved status
    LaunchedEffect(Unit) {
        movie = fetchMovieDetails(imdbId)

        // Check if movie exists in DB
        val laterList = dao.getMoviesByCategory("later")
        val watchedList = dao.getMoviesByCategory("watched")

        isWatchLater = laterList.any { it.imdbId == imdbId }
        isWatched = watchedList.any { it.imdbId == imdbId }

        loading = false
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator(color = PrimaryBlue)
        }
        return
    }

    movie?.let { m ->

        Box(
            Modifier
                .fillMaxSize()
                .background(PrimaryBlack)
        ) {

            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {

                // ======================================
                // Banner + Back Arrow (with background)
                // ======================================
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(m.poster),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        contentScale = ContentScale.Crop
                    )

                    // BACK BUTTON WITH SHADED BACKGROUND
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(42.dp)
                            .background(Color(0x66000000), RoundedCornerShape(50))
                            .align(Alignment.TopStart)
                            .clickable { navController.navigateUp() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Column(Modifier.padding(16.dp)) {

                    // ========================
                    // Title + Basic Meta
                    // ========================
                    Text(
                        m.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    )

                    Spacer(Modifier.height(6.dp))
                    Text(m.genre, color = Color.Gray)
                    Text("Released: ${m.released}", color = Color.Gray)

                    Spacer(Modifier.height(14.dp))

                    // IMDb, Runtime, Rating
                    InfoCard {
                        Text(
                            "⭐ IMDb Rating: ${m.imdbRating} (${m.imdbVotes} votes)",
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                        Text("⏳ Runtime: ${m.runtime}", color = Color.White)
                        Text("🔞 Rated: ${m.rating}", color = Color.White)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Plot / Story
                    InfoCard {
                        Text(
                            "Storyline",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            m.plot,
                            color = Color.LightGray,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Director / Writer / Actors
                    InfoCard {
                        Text("🎬 Director: ${m.director}", color = Color.White)
                        Text("✍️ Writer: ${m.writer}", color = Color.White)
                        Text("⭐ Cast: ${m.actors}", color = Color.White)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Awards
                    InfoCard {
                        Text("🏆 Awards", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(m.awards, color = Color(0xFFFFD700))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Box Office
                    InfoCard {
                        Text("💰 Box Office", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(m.boxOffice, color = Color.Green)
                    }

                    Spacer(Modifier.height(25.dp))

                    // ======================================
                    // Watch Later + Watched Buttons
                    // ======================================
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // WATCH LATER BUTTON
                        Button(
                            onClick = {
                                scope.launch {
                                    saveMovieToDb(dao, m, "later")
                                    isWatchLater = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isWatchLater) PrimaryBlue else Color.DarkGray
                            ),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Watch Later")
                        }

                        Spacer(Modifier.width(16.dp))

                        // WATCHED BUTTON
                        Button(
                            onClick = {
                                scope.launch {
                                    saveMovieToDb(dao, m, "watched")
                                    isWatched = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isWatched) Color(0xFF4CAF50) else Color.DarkGray
                            ),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Watched")
                        }
                    }

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}


@Composable
fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F1E24), RoundedCornerShape(16.dp))
            .padding(16.dp),
        content = content
    )
}


suspend fun saveMovieToDb(
    dao: MovieDao,
    movie: MovieDetails,
    category: String
) {
    dao.insertMovie(
        MovieEntity(
            imdbId = movie.imdbId,
            title = movie.title,
            poster = movie.poster,
            plot = movie.plot,
            genre = movie.genre,
            released = movie.released,
            category = category
        )
    )
}


suspend fun fetchMovieDetails(id: String): MovieDetails {
    val url = "https://www.omdbapi.com/?apikey=$OMDB_KEY&i=$id&plot=full"

    return withContext(Dispatchers.IO) {
        val json = JSONObject(URL(url).readText())

        MovieDetails(
            imdbId = json.getString("imdbID"),
            title = json.getString("Title"),
            poster = json.getString("Poster"),
            genre = json.getString("Genre"),
            released = json.getString("Released"),
            plot = json.getString("Plot"),
            runtime = json.optString("Runtime", "N/A"),
            rating = json.optString("Rated", "N/A"),
            imdbRating = json.optString("imdbRating", "N/A"),
            imdbVotes = json.optString("imdbVotes", "N/A"),
            director = json.optString("Director", "N/A"),
            writer = json.optString("Writer", "N/A"),
            actors = json.optString("Actors", "N/A"),
            awards = json.optString("Awards", "N/A"),
            boxOffice = json.optString("BoxOffice", "N/A")
        )
    }
}


data class MovieDetails(
    val imdbId: String,
    val title: String,
    val poster: String,
    val genre: String,
    val released: String,
    val plot: String,
    val runtime: String,
    val rating: String,
    val imdbRating: String,
    val imdbVotes: String,
    val director: String,
    val writer: String,
    val actors: String,
    val awards: String,
    val boxOffice: String
)
