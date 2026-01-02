package vijaysaiapp.project3493864.movietracker.fragments


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import vijaysaiapp.project3493864.movietracker.data.MovieDatabase
import vijaysaiapp.project3493864.movietracker.data.MovieEntity
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlue

@Composable
fun WatchListScreen(navController: NavHostController) {

    val dao = MovieDatabase.getDatabase(LocalContext.current).movieDao()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("later") }
    var movies by remember { mutableStateOf(listOf<MovieEntity>()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        movies = dao.getMoviesByCategory(selectedTab)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "My Watchlist",
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF3A3A40), CircleShape)
                    .clickable {
                        shareMovies(context, selectedTab, movies)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            WatchTabButton(
                label = "Watch Later",
                selected = selectedTab == "later",
                modifier = Modifier.weight(1f)
            ) {
                selectedTab = "later"
            }

            WatchTabButton(
                label = "Watched",
                selected = selectedTab == "watched",
                modifier = Modifier.weight(1f)
            ) {
                selectedTab = "watched"
            }
        }

        Spacer(Modifier.height(16.dp))

        if (movies.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No movies added yet", color = Color.Gray)
            }
            return
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {

            items(movies) { movie ->

                MovieWatchCard(
                    movie = movie,
                    onDelete = {
                        scope.launch {
                            dao.deleteMovie(movie.imdbId)
                            movies = dao.getMoviesByCategory(selectedTab)
                        }
                    },
                    onClick = {
                        navController.navigate("details/${movie.imdbId}")
                    }
                )
            }
        }
    }
}


fun shareMovies(context: android.content.Context, category: String, movies: List<MovieEntity>) {

    if (movies.isEmpty()) return

    val title = if (category == "later") "My Watch Later Movies:" else "My Watched Movies:"

    val movieList = movies.joinToString("\n") {
        "• ${it.title}"
    }

    val shareText = "$title\n\n$movieList"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "My Movie List")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(Intent.createChooser(intent, "Share using"))
}


@Composable
fun MovieWatchCard(
    movie: MovieEntity,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color(0xFF26252B), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {

        Column {

            Image(
                painter = rememberAsyncImagePainter(movie.poster),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            Text(
                movie.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(28.dp)
                .background(Color.Red, CircleShape)
                .clickable {
                    onDelete()
                           },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun WatchTabButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) PrimaryBlue else Color.DarkGray
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
    ) {
        Text(label, color = Color.White)
    }
}



