package vijaysaiapp.project3493864.movietracker.fragments

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlue


@Composable
fun ContactUsScreen(
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFF2D2C33), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = "Contact",
                tint = PrimaryBlue,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Contact Us",
            color = Color.White,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Have questions or suggestions?\nWe’d love to hear from you.",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(30.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(
                        Intent.ACTION_SENDTO,
                        Uri.parse("mailto:vijaysaiboya864@gmail.com")
                    )
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF26252B))
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Email",
                    tint = PrimaryBlue
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Email",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "vijaysaiboya864@gmail.com",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
