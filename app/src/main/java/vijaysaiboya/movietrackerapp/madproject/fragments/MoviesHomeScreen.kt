package vijaysaiboya.movietrackerapp.madproject.fragments


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
                        poster = obj.getString("Poster")
                    )
                )
            }
        } finally {
            conn.disconnect()
        }

        movies
    }
}

@Composable
fun MovieHomeScreen() {

    val heroHeight = (LocalConfiguration.current.screenHeightDp * 0.75f).dp

    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var currentIndex by remember { mutableStateOf(0) }

    // 🔥 Load Movies from API
    LaunchedEffect(Unit) {
        movies = fetchTopMovies()
    }

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

        if (movies.isNotEmpty()) {

            val movie = movies[currentIndex]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heroHeight)
            ) {

                Image(
                    painter = rememberAsyncImagePainter(movie.poster),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

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
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    movie.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("See Details")
                }

                Spacer(Modifier.height(18.dp))

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
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }
    }
}




