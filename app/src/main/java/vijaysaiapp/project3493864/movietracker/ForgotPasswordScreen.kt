package vijaysaiapp.project3493864.movietracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlue

@Composable
fun ForgotPasswordScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var step2 by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val context = LocalContext.current.findActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
    ) {

        Spacer(modifier = Modifier.height(94.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Forgot Password?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Reset it now!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(64.dp))

        if (!step2) {

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Enter Email Address",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.evergreen),
                    focusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Enter Date of Birth",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = dob,
                onValueChange = { dob = it },
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("dd-mm-yyyy") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.evergreen),
                    focusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    loading = true
                    errorMessage = ""
                    successMessage = ""

                    val key = email.replace(".", ",")

                    FirebaseDatabase.getInstance().getReference("FanAccounts").child(key).get()
                        .addOnSuccessListener { snapshot ->
                            loading = false

                            if (!snapshot.exists()) {
                                errorMessage = "User not found"
                                return@addOnSuccessListener
                            }

                            val dbEmail = snapshot.child("email").value?.toString() ?: ""
                            val dbDob = snapshot.child("dob").value?.toString() ?: ""

                            if (dbEmail == email && dbDob == dob) {
                                step2 = true
                            } else {
                                errorMessage = "Email or DOB incorrect"
                            }
                        }
                        .addOnFailureListener {
                            loading = false
                            errorMessage = "Error: ${it.localizedMessage}"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Verify",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        if (step2) {

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Enter New Password",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = newPassword,
                onValueChange = { newPassword = it },
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("New Password") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.evergreen),
                    focusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Confirm Password",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Confirm Password") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.evergreen),
                    focusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    errorMessage = ""
                    successMessage = ""

                    if (newPassword != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }

                    loading = true
                    val key = email.replace(".", ",")

                    FirebaseDatabase.getInstance().getReference("FanAccounts")
                        .child(key).child("password").setValue(newPassword)
                        .addOnSuccessListener {
                            loading = false
                            successMessage = "Password updated successfully!"

                            navController.popBackStack()
                        }
                        .addOnFailureListener {
                            loading = false
                            errorMessage = "Failed to update password"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Update Password",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (loading)
            Text("Processing...", color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))

        if (errorMessage.isNotEmpty())
            Text(errorMessage, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))

        if (successMessage.isNotEmpty())
            Text(successMessage, color = Color.Green, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
