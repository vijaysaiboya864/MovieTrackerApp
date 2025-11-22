package vijaysaiboya.movietrackerapp.madproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import vijaysaiboya.movietrackerapp.madproject.fragments.Film
import vijaysaiboya.movietrackerapp.madproject.fragments.FilmsScreen

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}


@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController)
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Films : BottomNavItem("films", "Films", Icons.Default.PlayArrow)
    object Watched : BottomNavItem("watched", "Watched", Icons.Default.PlayArrow)
    object WatchLater : BottomNavItem("watchlater", "Watch Later", Icons.Default.PlayArrow)
}




@Composable
fun WatchedMoviesScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Watched Movies List")
    }
}

@Composable
fun WatchLaterScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Watch Later Movies List")
    }
}


@Composable
fun NavigationGraph(navController: NavHostController) {

    val sampleFilms = listOf(
        Film("1", "The Shawshank Redemption", 1994, "https://images.jdmagicbox.com/comp/jd_social/news/2018jul21/image-119206-zkypi64x2m.jpg"),
        Film("2", "The Dark Knight", 2008, "https://example.com/darkknight.jpg"),
        Film("3", "Pulp Fiction", 1994, "https://example.com/pulpfiction.jpg"),
        Film("4", "The Lord of the Rings: The Return of the King", 2003, "https://example.com/lotr.jpg"),
        Film("5", "Forrest Gump", 1994, "https://example.com/forrestgump.jpg")
    )

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Films.route
    ) {
        composable(BottomNavItem.Films.route) { FilmsScreen(sampleFilms) }
        composable(BottomNavItem.Watched.route) { WatchedMoviesScreen() }
        composable(BottomNavItem.WatchLater.route) { WatchLaterScreen() }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Films,
        BottomNavItem.Watched,
        BottomNavItem.WatchLater
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                }
            )
        }
    }
}