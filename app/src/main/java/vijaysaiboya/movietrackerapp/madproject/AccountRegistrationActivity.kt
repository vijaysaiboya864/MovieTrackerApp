package vijaysaiboya.movietrackerapp.madproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

class AccountRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountRegistrationScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountRegistrationScreenPreview() {
    AccountRegistrationScreen()
}

@Composable
fun AccountRegistrationScreen() {
    var fanName by remember { mutableStateOf("") }
    var fanCountry by remember { mutableStateOf("") }
    var fanEmail by remember { mutableStateOf("") }
    var fanPassword by remember { mutableStateOf("") }


    val context = LocalContext.current.findActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.evergreen))
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
            text = "Enter Name",
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
            value = fanName,
            onValueChange = { fanName = it },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = colorResource(id = R.color.evergreen),
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.evergreen),
                focusedIndicatorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = "Enter Country",
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
            value = fanCountry,
            onValueChange = { fanCountry = it },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = colorResource(id = R.color.evergreen),
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.evergreen),
                focusedIndicatorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                unfocusedContainerColor = colorResource(id = R.color.evergreen),
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
                unfocusedContainerColor = colorResource(id = R.color.evergreen),
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.evergreen),
                focusedIndicatorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {
                when {
                    fanName.isEmpty() -> {
                        Toast.makeText(context, " Please Enter Name", Toast.LENGTH_SHORT).show()
                    }
                    fanCountry.isEmpty() ->{
                        Toast.makeText(context, " Please Enter Country", Toast.LENGTH_SHORT).show()
                    }

                    fanEmail.isEmpty() -> {
                        Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                    }

                    fanPassword.isEmpty() -> {
                        Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        val fanData = FanData(
                            name = fanName,
                            email = fanEmail,
                            country = fanCountry,
                            password = fanPassword
                        )
                        registerAccount(fanData,context!!)
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.misty_mint),
                contentColor = colorResource(
                    id = R.color.evergreen
                )
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )
        }

        Spacer(modifier = Modifier.height(64.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Spacer(
                modifier = Modifier
                    .weight(1f) // Width of the line
                    .height(2.dp) // Adjust height as needed
                    .padding(horizontal = 6.dp)
                    .background(Color.White) // Color of the line

            )

            Text(
                text = "or Login",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.clickable {
                    context!!.startActivity(Intent(context, SessionActivity::class.java))
                    context.finish()
                }
            )

            Spacer(
                modifier = Modifier
                    .weight(1f) // Width of the line
                    .height(2.dp) // Adjust height as needed
                    .padding(horizontal = 6.dp)
                    .background(Color.White) // Color of the line

            )

        }


    }

}

private fun registerAccount(fanData: FanData,context: Context){
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference("FanAccounts")
    ref.child(fanData.email.replace(".", ",")).setValue(fanData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                context.startActivity(
                    Intent(
                        context,
                        SessionActivity::class.java
                    )
                )
                (context as Activity).finish()
            } else {
                Toast.makeText(
                    context,
                    "User Registration Failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(
                context,
                "User Registration Failed: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
}


data class FanData
    (
    var name: String = "",
    var country: String ="",
    var email: String ="",
    var password: String ="",
)