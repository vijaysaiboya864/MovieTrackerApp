package vijaysaiapp.project3493864.movietracker

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlack
import vijaysaiapp.project3493864.movietracker.ui.theme.PrimaryBlue
import java.security.MessageDigest


@Preview(showBackground = true)
@Composable
fun SessionActivityScreenPreview() {
    SessionActivityScreen(navController = NavHostController(LocalContext.current))
}

@Composable
fun SessionActivityScreen(navController: NavController) {
    var fanEmail by remember { mutableStateOf("") }
    var fanPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }




    val context = LocalContext.current.findActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
    ) {
        Spacer(modifier = Modifier.height(94.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Welcome,",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Glad to see you!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = "Enter Email Address",
            color = Color.White
        )

        Spacer(
            modifier = Modifier
                .height(6.dp)
                .align(Alignment.Start)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            value = fanEmail,
            onValueChange = { fanEmail = it },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.evergreen),
                focusedIndicatorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = "Enter Password",
            color = Color.White
        )

        Spacer(
            modifier = Modifier
                .height(6.dp)
                .align(Alignment.Start)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            value = fanPassword,
            onValueChange = { fanPassword = it },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.evergreen),
                focusedIndicatorColor = Color.White
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        tint = Color.White
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                when {
                    fanEmail.isEmpty() -> {
                        Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                    }

                    fanPassword.isEmpty() -> {
                        Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {

                        val fanData = FanData(
                            email = fanEmail,
                            password = fanPassword
                        )
                        loginFanAccount(
                            fanData.email,
                            userPassword = fanData.password,
                            context!!,
                            navController
                        )

                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "I Forgot My Password",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                navController.navigate(AppScreens.ForgotPassword.route)
            }
        )

        Spacer(modifier = Modifier.height(64.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .padding(horizontal = 6.dp)
                    .background(Color.White)

            )

            Text(
                text = "or Register",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.clickable {

                    navController.navigate(AppScreens.Register.route) {
                        popUpTo(AppScreens.Login.route) {
                            inclusive = true
                        }
                    }

                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .padding(horizontal = 6.dp)
                    .background(Color.White)

            )

        }


    }

}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun loginFanAccount(
    userEmail: String,
    userPassword: String,
    context: Context,
    navController: NavController
) {
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    val sanitizedEmail = userEmail.replace(".", ",")
    val hashedPassword = sha256(userPassword)

    databaseReference.child("FanAccounts").child(sanitizedEmail).get()
        .addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val fanData = snapshot.getValue(FanData::class.java)
                fanData?.let {

                    if (hashedPassword == it.password) {

                        MovieTrackerPrefs.setLoginStatus(context, true)
                        MovieTrackerPrefs.saveUserEmail(
                            context,
                            email = userEmail
                        )
                        MovieTrackerPrefs.saveUserName(context, it.name)
                        MovieTrackerPrefs.saveUserDob(context, it.dob)
                        MovieTrackerPrefs.saveUserCountry(context, it.country)


                        Toast.makeText(context, "Login Successfull", Toast.LENGTH_SHORT).show()

                        navController.navigate(AppScreens.Home.route) {
                            popUpTo(AppScreens.Login.route) {
                                inclusive = true
                            }
                        }

                    } else {
                        Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "No User Found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            println("Error retrieving data: ${exception.message}")
        }
}
fun sha256(input: String): String {
    return MessageDigest
        .getInstance("SHA-256")
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}
