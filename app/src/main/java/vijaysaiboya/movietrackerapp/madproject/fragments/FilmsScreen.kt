package vijaysaiboya.movietrackerapp.madproject.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

data class Film(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val posterUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsScreen(
    films: List<Film>,
    onFilmClick: (Film) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Movie Tracker", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search movies"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // If the list of films is empty, show a message
        if (films.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No movies found. Try searching!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Display the list of films using LazyColumn for efficient scrolling
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(films, key = { it.id }) { film ->
                    FilmCard(film = film, onClick = { onFilmClick(film) })
                }
            }
        }
    }
}


@Composable
fun FilmCard(film: Film, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Fixed height for consistency
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                // Using Coil for image loading. For a real app, replace ColorPainter with rememberAsyncImagePainter(film.posterUrl)
                painter = rememberAsyncImagePainter(
                    model = film.posterUrl,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.primaryContainer), // Placeholder color
                    error = ColorPainter(MaterialTheme.colorScheme.errorContainer) // Error color
                ),
                contentDescription = "${film.title} Poster",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f), // Takes up remaining width
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2 // Allow title to wrap to two lines
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Released: ${film.releaseYear}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun FilmsScreenPreview() {
    val sampleFilms = listOf(
        Film("1", "The Shawshank Redemption", 1994, "https://images.jdmagicbox.com/comp/jd_social/news/2018jul21/image-119206-zkypi64x2m.jpg"),
        Film("2", "The Dark Knight", 2008, "https://example.com/darkknight.jpg"),
        Film("3", "Pulp Fiction", 1994, "https://example.com/pulpfiction.jpg"),
        Film("4", "The Lord of the Rings: The Return of the King", 2003, "https://example.com/lotr.jpg"),
        Film("5", "Forrest Gump", 1994, "https://example.com/forrestgump.jpg")
    )
    MaterialTheme {
        FilmsScreen(films = sampleFilms)
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun FilmsScreenEmptyPreview() {
    MaterialTheme {
        FilmsScreen(films = emptyList())
    }
}
