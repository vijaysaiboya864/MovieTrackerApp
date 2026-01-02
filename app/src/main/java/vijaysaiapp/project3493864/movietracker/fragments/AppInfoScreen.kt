package vijaysaiapp.project3493864.movietracker.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack


@Composable
fun AppInfoScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row() {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0x66000000), RoundedCornerShape(50))
                    .clickable { navController.navigateUp() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(20.dp))

            Text(
                text = "About Us",
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF26252B))
        ) {
            Column(Modifier.padding(20.dp)) {

                Text(
                    text = "The Movie Tracker and Watchlist App lets users follow the movies that they have already watched, generate watchlists, and browse new movies.",
                    color = Color.LightGray,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(20.dp))

                Divider(color = Color.DarkGray)

                Spacer(Modifier.height(16.dp))

                InfoRow(label = "Student Name", value = "Vijay Sai Boya")
                Spacer(Modifier.height(12.dp))
                InfoRow(label = "Student Number", value = "S3493864")
                Spacer(Modifier.height(12.dp))
                InfoRow(label = "Email", value = "vijaysaiboya864@gmail.com")
            }
        }

        ContactUsScreen()
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
