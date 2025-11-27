package vijaysaiboya.movietrackerapp.madproject

import android.content.Intent
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
import androidx.compose.runtime.SideEffect
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnBoardingScreen(::validateStatus)
        }
    }

    private fun validateStatus(studentStatus: Int) {
        if (studentStatus == 2) {
            startActivity(Intent(this, SessionActivity::class.java))
            finish()
        }

    }
}

@Composable
fun OnBoardingScreen(onLoginClick: (studentStatus: Int) -> Unit) {
    val context = LocalContext.current

    SideEffect {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            onLoginClick.invoke(2)
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
            .background(color = colorResource(id = R.color.evergreen)),
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
                        .background(color = Color.Transparent),
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
                        color = colorResource(id = R.color.evergreen), // Green color similar to the design
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = "Saved Movies",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.evergreen), // Green color similar to the design
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = "Super Hit Movies",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.evergreen), // Green color similar to the design
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