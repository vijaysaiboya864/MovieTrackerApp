package vijaysaiboya.movietrackerapp.madproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppNavGraph()
        }
    }


}

@Composable
fun MyAppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {
        composable(AppScreens.Splash.route) {
            OnBoardingScreen(navController = navController)
        }

        composable(AppScreens.Login.route) {
            SessionActivityScreen(navController = navController)
        }

        composable(AppScreens.Register.route) {
            AccountRegistrationScreen(navController = navController)
        }

        composable(AppScreens.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(AppScreens.Home.route) {
            HomeScreen(homeNanController = navController)
        }

    }

}


@Composable
fun OnBoardingScreen(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000)

        if (UserPrefs.checkLoginStatus(context)) {
            navController.navigate(AppScreens.Home.route) {
                popUpTo(AppScreens.Splash.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(AppScreens.Login.route) {
                popUpTo(AppScreens.Splash.route) {
                    inclusive = true
                }
            }
        }

    }

    SplashScrOnBoardingScreenD()
}

@Composable
fun SplashScrOnBoardingScreenD() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(94.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Vijay Sai",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )


            Card(
                modifier = Modifier
                    .padding(16.dp)
            )
            {

                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .background(color = PrimaryBlue),
                )
                {
                    Image(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.ic_movie_tracker),
                        contentDescription = "Movie Tracking App",
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Track",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black), // Green color similar to the design
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = "Latest Movies",
                        fontWeight = FontWeight.Bold,
                        color = Color.White, // Green color similar to the design
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = "Saved Movies",
                        fontWeight = FontWeight.Bold,
                        color = Color.White, // Green color similar to the design
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = "Super Hit Movies",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScrOnBoardingScreenDPreview() {
    SplashScrOnBoardingScreenD()
}