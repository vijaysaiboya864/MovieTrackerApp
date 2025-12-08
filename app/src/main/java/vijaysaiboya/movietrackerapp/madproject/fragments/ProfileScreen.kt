package vijaysaiboya.movietrackerapp.madproject.fragments


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vijaysaiboya.movietrackerapp.madproject.AppScreens
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack

@Composable
fun ProfileScreen(
    navController: NavHostController,
    username: String = "username",
    email: String = "email",
    onLogout: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ==============================
        // Profile Icon
        // ==============================
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFF2D2C33), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // ==============================
        // User Info Card
        // ==============================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF26252B))
        ) {
            Column(
                Modifier.padding(20.dp)
            ) {

                Text(
                    "Username",
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )

                Text(
                    username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Email",
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )

                Text(
                    email,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        // ==============================
        // Logout Button
        // ==============================
        Button(
            onClick = {
                onLogout()        // Clear login session
                navController.navigate(AppScreens.Login.route) {
                    popUpTo(0) { inclusive = true }  // Clear backstack
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = Color.White
            )
            Spacer(Modifier.width(10.dp))
            Text("Logout", color = Color.White)
        }
    }
}
