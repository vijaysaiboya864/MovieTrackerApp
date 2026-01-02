package vijaysaiapp.project3493864.movietracker.fragments

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import vijaysaiapp.project3493864.movietracker.AppScreens
import vijaysaiapp.project3493864.movietracker.MovieTrackerPrefs
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserFeedback(
    val message: String = "",
    val type: String = "Feedback",
    val timestamp: Long = 0L
)


@Composable
fun FeedbackScreen(
    navController: NavHostController
) {

    val context = LocalContext.current
    val userEmail = MovieTrackerPrefs.getUserEmail(context)

    var feedbackText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Feedback") }

    var feedbacks by remember { mutableStateOf(listOf<UserFeedback>()) }
    var loading by remember { mutableStateOf(true) }

    val ref = FirebaseDatabase.getInstance()
        .getReference("UserFeedbacks")
        .child(userEmail.replace(".", ","))

    LaunchedEffect(Unit) {
        ref.get().addOnSuccessListener { snapshot ->
            val list = mutableListOf<UserFeedback>()
            snapshot.children.forEach { child ->
                val feedback = child.getValue(UserFeedback::class.java)
                feedback?.let { list.add(it) }
            }
            feedbacks = list.sortedByDescending { it.timestamp }
            loading = false
        }.addOnFailureListener {
            loading = false
        }
    }

    Column(
        modifier = Modifier
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
                "Feedback & Issues",
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF3A3A40), CircleShape)
                    .clickable {
                        navController.navigate(AppScreens.AppInfoScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "AboutUs",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(16.dp))


        Column(
            modifier= Modifier.fillMaxSize().padding(20.dp)
        ) {


            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                FeedbackTypeChip(
                    label = "Feedback",
                    selected = selectedType == "Feedback"
                ) { selectedType = "Feedback" }

                FeedbackTypeChip(
                    label = "Issue",
                    selected = selectedType == "Issue"
                ) { selectedType = "Issue" }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                placeholder = { Text("Write your feedback or report an issue...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
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
                    if (feedbackText.isBlank()) return@Button

                    val feedback = UserFeedback(
                        message = feedbackText,
                        type = selectedType,
                        timestamp = System.currentTimeMillis()
                    )

                    val id = ref.push().key ?: return@Button

                    ref.child(id).setValue(feedback)
                        .addOnSuccessListener {
                            feedbacks = listOf(feedback) + feedbacks
                            feedbackText = ""
                            Toast.makeText(context, "Submitted successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "My Previous Feedbacks",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            when {
                loading -> {
                    CircularProgressIndicator(color = PrimaryBlue)
                }

                feedbacks.isEmpty() -> {
                    Text(
                        "No feedback submitted yet",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    feedbacks.forEach { feedback ->
                        FeedbackItem(feedback)
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun FeedbackTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) PrimaryBlue else Color.DarkGray,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(label, color = Color.White)
    }
}

@Composable
fun FeedbackItem(feedback: UserFeedback) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF26252B), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                feedback.type,
                color = if (feedback.type == "Issue") Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )

            Text(
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(Date(feedback.timestamp)),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            feedback.message,
            color = Color.White
        )
    }
}
