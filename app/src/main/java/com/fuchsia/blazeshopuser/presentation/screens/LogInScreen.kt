package com.fuchsia.blazeshopuser.presentation.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@SuppressLint("UnrememberedMutableState", "ResourceAsColor")
@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val context = LocalContext.current

    val logInState = viewModel.logInState.collectAsState()

    var backPressedOnce by remember { mutableStateOf(false) }
    BackHandler {
        if (backPressedOnce) {
            (context as? ComponentActivity)?.finish()
        } else {
            backPressedOnce = true
        }
    }

    when {
        logInState.value.isLoading -> {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AlertDialog(
                    onDismissRequest = { /*TODO*/ },
                    text = { CircularProgressIndicator() },
                    confirmButton = { /*TODO*/ },
                    dismissButton = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                )
            }

        }

        logInState.value.error != null -> {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Toast.makeText(context, logInState.value.error!!, Toast.LENGTH_SHORT).show()

            }
        }

        logInState.value.isSuccess != null -> {
            navController.navigate(
                Routes.SuccessfulRegScreen(
                    regSuccess = "0"
                )
            )

        }
    }


    Box(modifier = modifier.fillMaxSize()) {

        Image(
            modifier = Modifier
                .size(220.dp)
                .align(
                    alignment = Alignment.TopEnd
                ), painter = painterResource(id = R.drawable.ellipsetop), contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(150.dp)

                .align(
                    alignment = Alignment.BottomStart
                ),
            painter = painterResource(id = R.drawable.ellipsebottom),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(30.dp)
        ) {

            Text(
                text = "Log In",
                modifier.padding(top = 80.dp),
                style = MaterialTheme.typography.headlineLarge,
            )

            OutlinedTextField(modifier = modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(20.dp),


                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                ), leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }, singleLine = true, label = {
                    Text(
                        text = "Email",
                        color = Color.Gray
                    )
                })

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray

                    )
                },
                shape = RoundedCornerShape(20.dp),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFEBF5FC),
                    unfocusedContainerColor = Color(0xFFEBF5FC),
                    focusedIndicatorColor = Color(0xFFF68B8B),
                    unfocusedIndicatorColor = Color(0xFFF68B8B),
                ),
                singleLine = true,
                label = {
                    Text(
                        text = "Password",
                        color = Color.Gray

                    )
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Color.Gray,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {

                        val userData = UserDataModel(
                            email = email.value,
                            password = password.value
                        )
                        viewModel.logInUser(userData)
                    }

                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF68B8B),

                    )

            ) {
                Text(
                    text = "Log In",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.Gray
                )
                Text(
                    modifier = modifier.clickable {
                        navController.navigate(Routes.SignUpScreen)

                    },
                    text = " Sign Up",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF68B8B)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(Color.Black),
                )

                Text(
                    text = "    OR    ",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF68B8B)
                )

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(100.dp)
                        .background(Color.Black),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFFF68B8B),
                        shape = RoundedCornerShape(20.dp)
                    ),
                verticalAlignment = CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 25.dp),
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null
                )
                Text(
                    text = "Log in with Google",
                    color = Color.Gray,

                    modifier = Modifier.padding(start = 50.dp),
                )
            }
        }


    }

}


