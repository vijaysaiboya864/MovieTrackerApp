package vijaysaiboya.movietrackerapp.madproject.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import vijaysaiboya.movietrackerapp.madproject.AppScreens
import vijaysaiboya.movietrackerapp.madproject.DOBDateField
import vijaysaiboya.movietrackerapp.madproject.UserPrefs
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlack
import vijaysaiboya.movietrackerapp.madproject.ui.theme.PrimaryBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(
    navController: NavHostController,
    homeNavController: NavHostController,
    email: String,
    onLogout: () -> Unit = {}
) {

    val context = LocalContext.current

    var isEditing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        name = UserPrefs.getUserName(context)
        dob = UserPrefs.getUserDob(context)
        country = UserPrefs.getUserCountry(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // BACK BUTTON
        Box(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0x66000000), RoundedCornerShape(50))
                    .clickable { homeNavController.navigateUp() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
        }

        Spacer(Modifier.height(20.dp))

        // PROFILE ICON
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFF2D2C33), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(60.dp))
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF26252B))
        ) {

            Column(Modifier.padding(20.dp)) {

                ProfileRow("Name", name)

                Spacer(Modifier.height(12.dp))

                ProfileRow("Email", email)

                Spacer(Modifier.height(12.dp))


                AnimatedVisibility(
                    visible = isEditing,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Column {


                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Name",
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
                            value = name,
                            onValueChange = { name = it },
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                unfocusedContainerColor = Color.DarkGray,
                                unfocusedIndicatorColor = Color.White,
                                focusedContainerColor = Color.DarkGray,
                                focusedIndicatorColor = Color.White
                            )
                        )

                        Spacer(Modifier.height(6.dp))


                        DOBDateField(
                            label = "Date of Birth",
                            value = dob,
                            onClick = {
                                openDobPicker(context) { dob = it }
                            }
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Country",
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
                            value = country,
                            onValueChange = { country = it },
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                unfocusedContainerColor = Color.DarkGray,
                                unfocusedIndicatorColor = Color.White,
                                focusedContainerColor = Color.DarkGray,
                                focusedIndicatorColor = Color.White
                            )
                        )
                    }
                }

                if (!isEditing) {
                    Spacer(Modifier.height(12.dp))
                    ProfileRow("DOB", dob)
                    Spacer(Modifier.height(12.dp))
                    ProfileRow("Country", country)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ==============================
        // ACTION BUTTONS
        // ==============================
        if (isEditing) {

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Button(
                    onClick = {
                        // 🔥 FIREBASE UPDATE
                        val updates = mapOf(
                            "name" to name,
                            "dob" to dob,
                            "country" to country
                        )

                        FirebaseDatabase.getInstance()
                            .getReference("FanAccounts")
                            .child(email.replace(".", ","))
                            .updateChildren(updates)

                        // 🔥 USER PREF SYNC
                        UserPrefs.saveUserName(context, name)
                        UserPrefs.saveUserDob(context, dob)
                        UserPrefs.saveUserCountry(context, country)

                        isEditing = false
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }

                Button(
                    onClick = { isEditing = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }

        } else {
            Button(
                onClick = { isEditing = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, null)
                Spacer(Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }

        Spacer(Modifier.height(30.dp))

        // LOGOUT
        Button(
            onClick = {
                UserPrefs.clearUserSession(context)
                onLogout()
                navController.navigate(AppScreens.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Logout, null)
            Spacer(Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Text(label, color = Color.Gray, fontSize = 12.sp)
    Text(value, color = Color.White, fontWeight = FontWeight.Bold)
}

fun openDobPicker(
    context: Context,
    onSelect: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    val picker = DatePickerDialog(
        context,
        { _, year, month, day ->
            val c = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            onSelect(sdf.format(c.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // ✅ DOB = only past dates
    picker.datePicker.maxDate = System.currentTimeMillis()
    picker.show()
}
