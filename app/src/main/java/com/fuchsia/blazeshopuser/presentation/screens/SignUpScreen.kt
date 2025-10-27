package com.fuchsia.blazeshopuser.presentation.screens


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }

    val createUserState = viewModel.createUserState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main UI
        SignUpContent(
            modifier = modifier,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            navController = navController,
            onSignUpClick = {
                if (firstName.value.isNotEmpty() &&
                    lastName.value.isNotEmpty() &&
                    email.value.isNotEmpty() &&
                    password.value.isNotEmpty() &&
                    confirmPassword.value.isNotEmpty()
                ) {
                    if (password.value == confirmPassword.value) {
                        val userData = UserDataModel(
                            firstName = firstName.value,
                            lastName = lastName.value,
                            email = email.value,
                            password = password.value
                        )
                        viewModel.createUser(userData)
                    }
                }
            }
        )

        // Handle loading and error states (overlays on top)
        HandleCreateUserState(
            isLoading = createUserState.value.isLoading,
            error = createUserState.value.error,
            isSuccess = createUserState.value.isSuccess,
            navController = navController,
            modifier = modifier,
            viewModel
        )
    }

}
    // Separate composable for state handling
    @Composable
    private fun HandleCreateUserState(
        isLoading: Boolean,
        error: String?,
        isSuccess: String?,
        navController: NavController,
        modifier: Modifier,
        viewModel: MyViewModel
    ) {
        val context = LocalContext.current

        // Handle error with Toast
        LaunchedEffect(error) {
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Handle success navigation

        LaunchedEffect(isSuccess) {
            isSuccess?.let {
                navController.navigate(
                    Routes.SuccessfulRegScreen(regSuccess = "1")
                ) {
                    // Optional: Remove SignUpScreen from back stack
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                viewModel.resetCreateUserState()
            }
        }

        // Show loading indicator overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFF68B8B))
            }
        }
    }

// Main content composable
@Composable
private fun SignUpContent(
    modifier: Modifier,
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    email: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    navController: NavController,
    onSignUpClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImages()

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = 25.dp, start = 30.dp, end = 30.dp)
        ) {
            Text(
                text = "Sign Up",
                modifier = Modifier.padding(top = 80.dp),
                style = MaterialTheme.typography.headlineLarge,
            )

            NameFields(
                firstName = firstName,
                lastName = lastName,
                modifier = modifier
            )

            EmailField(
                email = email,
                modifier = modifier
            )

            PasswordFields(
                password = password,
                confirmPassword = confirmPassword,
                modifier = modifier
            )

            Spacer(modifier = Modifier.height(30.dp))

            SignUpButton(onSignUpClick = onSignUpClick)

            Spacer(modifier = Modifier.height(20.dp))

            LoginPrompt(
                navController = navController,
                modifier = modifier
            )

            Spacer(modifier = Modifier.height(10.dp))

            OrDivider(modifier = modifier)

            GoogleSignInButton()
        }
    }
}

// Background images - needs BoxScope context
@Composable
private fun BoxScope.BackgroundImages() {
    Image(
        modifier = Modifier
            .size(220.dp)
            .align(Alignment.TopEnd),
        painter = painterResource(id = R.drawable.ellipsetop),
        contentDescription = null
    )

    Image(
        modifier = Modifier
            .size(150.dp)
            .align(Alignment.BottomStart),
        painter = painterResource(id = R.drawable.ellipsebottom),
        contentDescription = null
    )
}

// Name input fields
@Composable
private fun NameFields(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        OutlinedTextField(
            modifier = modifier.weight(1f),
            value = firstName.value,
            onValueChange = { firstName.value = it },
            shape = RoundedCornerShape(20.dp),
            colors = textFieldColors(),
            singleLine = true,
            label = { Text(text = "First Name", color = Color.Gray) }
        )

        Spacer(modifier = Modifier.width(10.dp))

        OutlinedTextField(
            modifier = modifier.weight(1f),
            value = lastName.value,
            onValueChange = { lastName.value = it },
            shape = RoundedCornerShape(20.dp),
            colors = textFieldColors(),
            singleLine = true,
            label = { Text(text = "Last Name", color = Color.Gray) }
        )
    }
}

// Email field
@Composable
private fun EmailField(
    email: MutableState<String>,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        value = email.value,
        onValueChange = { email.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(20.dp),
        colors = textFieldColors(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        singleLine = true,
        label = { Text(text = "Email", color = Color.Gray) }
    )
}

// Password fields
@Composable
private fun PasswordFields(
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        value = password.value,
        onValueChange = { password.value = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = textFieldColors(),
        singleLine = true,
        label = { Text(text = "Create Password", color = Color.Gray) }
    )

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        value = confirmPassword.value,
        onValueChange = { confirmPassword.value = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = textFieldColors(),
        singleLine = true,
        label = { Text(text = "Confirm Password", color = Color.Gray) }
    )
}

// Sign up button
@Composable
private fun SignUpButton(onSignUpClick: () -> Unit) {
    Button(
        onClick = onSignUpClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF68B8B)
        )
    ) {
        Text(
            text = "Sign Up",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// Login prompt
@Composable
private fun LoginPrompt(
    navController: NavController,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Already have an account?", color = Color.Gray)
        Text(
            modifier = modifier.clickable { navController.popBackStack() },
            text = " Log In",
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF68B8B)
        )
    }
}

// OR divider
@Composable
private fun OrDivider(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(100.dp)
                .background(Color.Black)
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
                .background(Color.Black)
        )
    }
}

// Google sign in button
@Composable
private fun GoogleSignInButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
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
            modifier = Modifier.padding(start = 50.dp)
        )
    }
}

// Shared text field colors
@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedContainerColor = Color(0xFFEBF5FC),
    unfocusedContainerColor = Color(0xFFEBF5FC),
    focusedIndicatorColor = Color(0xFFF68B8B),
    unfocusedIndicatorColor = Color(0xFFF68B8B)
)