package vijaysaiboya.movietrackerapp.madproject


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import vijaysaiboya.movietrackerapp.madproject.fragments.MovieDetailsScreen
import vijaysaiboya.movietrackerapp.madproject.fragments.MovieHomeScreen
import vijaysaiboya.movietrackerapp.madproject.fragments.ProfileScreen
import vijaysaiboya.movietrackerapp.madproject.fragments.SearchMoviesScreen
import vijaysaiboya.movietrackerapp.madproject.fragments.WatchListScreen
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(homeNanController = NavHostController(LocalContext.current))
}


@Composable
fun HomeScreen(homeNanController: NavHostController) {
    val navController = rememberNavController()

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(PrimaryBlack, darkIcons = true)
    }

    Scaffold(
        bottomBar = {
            CustomBottomBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController,homeNanController)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController,homeNavController: NavHostController) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Films.route
    ) {
        composable(BottomNavItem.Films.route) { MovieHomeScreen(navController) }
        composable(BottomNavItem.Watched.route) { SearchMoviesScreen(navController) }

        composable("details/{id}") { backStack ->
            val imdbId = backStack.arguments?.getString("id") ?: ""
            MovieDetailsScreen(imdbId, navController)
        }

        composable(BottomNavItem.WatchLater.route) {
            WatchListScreen(navController)
        }

        composable(AppScreens.Profile.route) {
            ProfileScreen(
                navController = homeNavController,
                username = UserPrefs.getName(context),   // Replace with stored user data
                email = UserPrefs.getEmail(context),
                onLogout = {
                    UserPrefs.markLoginStatus(context,false)
                }
            )
        }

    }
}


sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Films : BottomNavItem("films", "Home", Icons.Outlined.Home)
    object Watched : BottomNavItem("watched", "Search", Icons.Outlined.Search)
    object WatchLater : BottomNavItem("watchlater", "Saved", Icons.Outlined.Favorite)
}


@Composable
fun CustomBottomBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Films,
        BottomNavItem.Watched,
        BottomNavItem.WatchLater
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        shadowElevation = 10.dp,
        color = PrimaryBlack
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->
                val selected = currentRoute == item.route

                BottomNavItemView(
                    item = item,
                    selected = selected
                ) {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Background color animation
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) PrimaryBlue else Color.Transparent,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    // Icon scale animation
    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.15f else 1f,
        animationSpec = tween(durationMillis = 250), label = ""
    )

    // Width animation
    val horizontalPadding by animateDpAsState(
        targetValue = if (selected) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(42.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = horizontalPadding)
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (selected) Color.White else Color.Gray,
            modifier = Modifier
                .size(22.dp)
                .graphicsLayer {
                    scaleX = iconScale
                    scaleY = iconScale
                }
        )

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(tween(200)) + expandHorizontally(tween(300)),
            exit = fadeOut(tween(150)) + shrinkHorizontally(tween(200))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.title,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}