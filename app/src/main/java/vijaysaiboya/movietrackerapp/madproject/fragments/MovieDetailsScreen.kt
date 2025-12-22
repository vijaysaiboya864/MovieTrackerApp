package vijaysaiboya.movietrackerapp.madproject.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import vijaysaiboya.movietrackerapp.madproject.UserPrefs
import vijaysaiboya.movietrackerapp.madproject.data.MovieDao
import vijaysaiboya.movietrackerapp.madproject.data.MovieDatabase
import vijaysaiboya.movietrackerapp.madproject.data.MovieEntity
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.net.HttpURLConnection
import java.net.URL
import kotlin.jvm.java

@Composable
fun MovieDetailsScreen(
    imdbId: String,
    navController: NavHostController
) {
    var movie by remember { mutableStateOf<MovieDetails?>(null) }
    var loading by remember { mutableStateOf(true) }

    var reviewsLoading by remember { mutableStateOf(true) }

    val dao = MovieDatabase.getDatabase(LocalContext.current).movieDao()
    val scope = rememberCoroutineScope()

    // Track saved state
    var isWatchLater by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var userRating by remember { mutableStateOf(0) }
    var userReview by remember { mutableStateOf("") }
    var reviews by remember { mutableStateOf(listOf<MovieReview>()) }
    var hasUserReviewed by remember { mutableStateOf(false) }

    val userEmail = UserPrefs.getUserEmail(context)
    val userName = UserPrefs.getUserName(context)


    // Load movie + saved status
    LaunchedEffect(Unit) {
        movie = fetchMovieDetails(imdbId)

        Log.e("Test","Movie Id - ${imdbId}")

        // Check if movie exists in DB
        val laterList = dao.getMoviesByCategory("later")
        val watchedList = dao.getMoviesByCategory("watched")

        isWatchLater = laterList.any { it.imdbId == imdbId }
        isWatched = watchedList.any { it.imdbId == imdbId }

        loading = false


        val reviewRef = FirebaseDatabase.getInstance()
            .getReference("MovieReviews")
            .child(imdbId)
        reviewRef.get().addOnSuccessListener { snapshot ->

            val list = mutableListOf<MovieReview>()

            if (snapshot.exists()) {
                snapshot.children.forEach { child ->
                    val review = child.getValue(MovieReview::class.java)
                    review?.let {
                        list.add(it)
                        if (it.userEmail == userEmail) {
                            hasUserReviewed = true
                        }
                    }
                }
            }

            reviews = list.sortedByDescending { it.timestamp }
            reviewsLoading = false   // ✅ ALWAYS stop loading
        }
            .addOnFailureListener {
                reviewsLoading = false   // ✅ Also stop on error
            }


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


                    Button(
                        onClick = {
                            openMovieTrailer(context, m.title)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935) // YouTube red
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Watch Trailer",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Watch Trailer",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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

                        // ==============================
                        // WATCH LATER BUTTON
                        // Disable if already Watched
                        // ==============================
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            // ==============================
                            // WATCH LATER BUTTON
                            // Disabled if movie is already Watched
                            // ==============================
                            Button(
                                onClick = {
                                    scope.launch {
                                        saveMovieToDb(dao, m, "later")
                                        isWatchLater = true
                                        isWatched = false
                                    }
                                },
                                enabled = !isWatched,   // ❌ Disabled if already Watched
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        isWatchLater -> PrimaryBlue
                                        isWatched -> Color.DarkGray.copy(alpha = 0.3f)
                                        else -> Color.DarkGray
                                    }
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
                                Text("Watch Later", color = Color.White)
                            }

                            Spacer(Modifier.width(16.dp))

                            // ==============================
                            // WATCHED BUTTON
                            // Always enabled
                            // ==============================
                            Button(
                                onClick = {
                                    scope.launch {
                                        saveMovieToDb(dao, m, "watched")
                                        isWatched = true
                                        isWatchLater = false
                                    }
                                },
                                enabled = !isWatched || isWatchLater,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isWatched)
                                        Color(0xFF4CAF50)
                                    else
                                        Color.DarkGray
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
                                Text("Watched", color = Color.White)
                            }
                        }

                    }

                    Spacer(Modifier.height(24.dp))

                    if(reviewsLoading)
                    {
                        Text("Loading Reviews", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))

                    }else {

                        if (!hasUserReviewed) {

                            InfoCard {

                                Text(
                                    "Rate & Review",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Spacer(Modifier.height(12.dp))

                                StarRating(
                                    rating = userRating,
                                    onRatingSelected = { userRating = it }
                                )

                                Spacer(Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = userReview,
                                    onValueChange = { userReview = it },
                                    placeholder = { Text("Write your review...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 4,
                                    colors = TextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        unfocusedContainerColor = Color.DarkGray,
                                        unfocusedIndicatorColor = Color.White,
                                        focusedContainerColor = Color.DarkGray,
                                        focusedIndicatorColor = Color.White
                                    )
                                )

                                Spacer(Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (userRating == 0 || userReview.isBlank()) return@Button

                                        val review = MovieReview(
                                            userEmail = userEmail,
                                            userName = userName,
                                            rating = userRating,
                                            review = userReview,
                                            timestamp = System.currentTimeMillis()
                                        )

                                        FirebaseDatabase.getInstance()
                                            .getReference("MovieReviews")
                                            .child(imdbId)
                                            .child(userEmail.replace(".", ","))
                                            .setValue(review)
                                            .addOnSuccessListener {
                                                hasUserReviewed = true
                                                reviews = listOf(review) + reviews
                                            }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Submit Review")
                                }
                            }
                        }


                        Spacer(Modifier.height(24.dp))

                        if (reviews.isNotEmpty()) {

                            Text(
                                "User Reviews",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(Modifier.height(12.dp))

                            reviews.forEach { review ->
                                ReviewItem(review)
                                Spacer(Modifier.height(12.dp))
                            }
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

fun openMovieTrailer(context: Context, movieTitle: String) {
    val query = Uri.encode("$movieTitle official trailer")
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.youtube.com/results?search_query=$query")
    )
    context.startActivity(intent)
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



// Movie Review Code

data class MovieReview(
    val userEmail: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val review: String = "",
    val timestamp: Long = 0L
)

@Composable
fun StarRating(
    rating: Int,
    onRatingSelected: (Int) -> Unit
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingSelected(i) }
            )
        }
    }
}

@Composable
fun ReviewItem(review: MovieReview) {
    InfoCard {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(review.userName, color = Color.White, fontWeight = FontWeight.Bold)
            Text("⭐ ${review.rating}", color = Color.Yellow)
        }

        Spacer(Modifier.height(6.dp))

        Text(
            review.review,
            color = Color.LightGray
        )
    }
}
