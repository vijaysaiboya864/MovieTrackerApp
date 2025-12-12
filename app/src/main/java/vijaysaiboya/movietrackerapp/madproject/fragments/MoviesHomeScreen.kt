package vijaysaiboya.movietrackerapp.madproject.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vijaysaiboya.movietrackerapp.madproject.AppScreens
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.net.HttpURLConnection
import java.net.URL

const val OMDB_KEY = "d157a6d6"

data class Movie(
    val title: String,
    val poster: String,
    val imdbId: String = "",
    val genre: String = ""
)

suspend fun fetchTopMovies(): List<Movie> {
    val url = "https://www.omdbapi.com/?apikey=$OMDB_KEY&s=top&type=movie"

    return withContext(Dispatchers.IO) {
        val movies = mutableListOf<Movie>()
        val conn = URL(url).openConnection() as HttpURLConnection

        try {
            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)

            if (!json.has("Search")) return@withContext emptyList()

            val results = json.getJSONArray("Search")

            for (i in 0 until results.length()) {
                val obj = results.getJSONObject(i)
                movies.add(
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

        movies
    }
}

// --------------------------------------------------------
// HOME SCREEN WITH NAVIGATION SUPPORT
// --------------------------------------------------------
@Composable
fun MovieHomeScreen(navController: NavHostController) {

    val heroHeight = (LocalConfiguration.current.screenHeightDp * 0.75f).dp

    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var currentIndex by remember { mutableStateOf(0) }

    // Load movies
    LaunchedEffect(Unit) {
        movies = fetchTopMovies()
    }

    // Auto slider
    LaunchedEffect(movies) {
        while (movies.isNotEmpty()) {
            delay(3000)
            currentIndex = (currentIndex + 1) % movies.size
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
    ) {

        // ==================================================
        // 1️⃣ HERO MOVIE SLIDER (Background)
        // ==================================================
        if (movies.isNotEmpty()) {

            val movie = movies[currentIndex]

            // ==========================
            // MOVIE SLIDER BOX
            // ==========================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heroHeight)
            ) {

                // Poster
                Image(
                    painter = rememberAsyncImagePainter(movie.poster),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    PrimaryBlack.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // ======================
                // LEFT ARROW BUTTON
                // ======================
                IconButton(
                    onClick = {
                        currentIndex =
                            if (currentIndex == 0) movies.lastIndex else currentIndex - 1
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                        .size(50.dp)
                        .background(Color(0x55000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous movie",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // ======================
                // RIGHT ARROW BUTTON
                // ======================
                IconButton(
                    onClick = {
                        currentIndex = (currentIndex + 1) % movies.size
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                        .size(50.dp)
                        .background(Color(0x55000000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next movie",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // ==========================
            // BOTTOM CONTENT
            // ==========================
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(
                    movie.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                // Details Button
                Button(
                    onClick = { navController.navigate("details/${movie.imdbId}") },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("See Details")
                }

                Spacer(Modifier.height(18.dp))

                // Slider Indicators (Dots)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(movies.size) { idx ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == currentIndex) 12.dp else 8.dp)
                                .background(
                                    if (idx == currentIndex) PrimaryBlue else Color.DarkGray,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
        else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }

        // ==================================================
        // 2️⃣ APP BAR — DRAWN LAST (Always on Top)
        // ==================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x44000000), RoundedCornerShape(30.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Movie Tracker",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0x55000000), CircleShape)
                    .clickable {
                        navController.navigate(AppScreens.Profile.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


