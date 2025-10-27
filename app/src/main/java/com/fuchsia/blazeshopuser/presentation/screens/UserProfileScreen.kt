package com.fuchsia.blazeshopuser.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import com.fuchsia.blazeshopuser.presentation.viewModel.MyViewModel

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {

    val addProfilePhotoState = viewModel.addProfilePhotoState.collectAsState()
    val updateUserState = viewModel.updateUserState.collectAsState()

    val showUi = remember { mutableStateOf(false) }

    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        CustomDialogLogOut(

            navController = navController,
            openDialog
        )
    }

    val imageUri = remember { mutableStateOf("") }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }

    val userDetailsState = viewModel.userDetailsState.collectAsState()
    val context = LocalContext.current

    val isEditProfile = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {
        if (userDetailsState.value.isSuccess == null) {
            viewModel.getUserDetails()
        }
    }

    LaunchedEffect(updateUserState.value.isSuccess) {
        updateUserState.value.isSuccess?.let {
            viewModel.getUserDetails()
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        uri?.let {
            imageUri.value = it.toString()
        } ?: run {
            imageUri.value = ""
        }
    }


    val logOutState = viewModel.logOutState.collectAsState()

    when {
        logOutState.value.isSuccess != null -> {
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
            navController.navigate(Routes.LogInScreen)
        }

    }

    when {
        userDetailsState.value.isLoading -> {

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

        userDetailsState.value.error != null -> {
            Toast.makeText(context, userDetailsState.value.error, Toast.LENGTH_SHORT).show()
        }

        userDetailsState.value.isSuccess != null -> {
            val user = userDetailsState.value.isSuccess!!

            LaunchedEffect(key1 = Unit) {
                firstName.value = user.firstName
                lastName.value = user.lastName
                email.value = user.email
                phoneNumber.value = user.phoneNumber
                address.value = user.address
                password.value = user.password
                if (user.profilePicture.isNotEmpty()) {
                    imageUri.value = user.profilePicture
                }
            }
            showUi.value = true

        }
    }

    when {
        addProfilePhotoState.value.isLoading -> {
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

        addProfilePhotoState.value.error != null -> {
            Toast.makeText(context, addProfilePhotoState.value.error, Toast.LENGTH_SHORT).show()

        }

        addProfilePhotoState.value.isSuccess != null -> {

            LaunchedEffect(addProfilePhotoState.value.isSuccess) {

                val updatedUser = UserDataModel(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    email = email.value,
                    password = password.value,
                    phoneNumber = phoneNumber.value,
                    address = address.value,
                    profilePicture = addProfilePhotoState.value.isSuccess!!
                )

                viewModel.updateUser(updatedUser)
            }
        }
    }

    when {
        updateUserState.value.isLoading -> {
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

        updateUserState.value.error != null -> {
            Toast.makeText(context, updateUserState.value.error, Toast.LENGTH_SHORT).show()
        }

        updateUserState.value.isSuccess != null -> {

            isEditProfile.value = false

            LaunchedEffect(key1 = Unit) {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateUserState()

            }

        }

    }

    if (showUi.value) {

        Box(modifier = modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .padding(start = 28.dp, top = 20.dp)
                    .align(Alignment.TopStart)

            ) {

                AsyncImage(
                    model = if (imageUri.value.isNotEmpty()) Uri.parse(imageUri.value) else R.drawable.upload,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(120.dp)
                        .border(1.dp, Color(0xFFF68B8B), CircleShape)
                        .clickable {
                            if (isEditProfile.value) {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }

                        },
                    contentScale = ContentScale.Crop


                )
            }



            Image(
                modifier = Modifier
                    .padding(25.dp)
                    .size(40.dp)
                    .align(
                        alignment = Alignment.TopEnd
                    )
                    .clickable {
                        openDialog.value = true
                    },
                painter = painterResource(id = R.drawable.logout),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(top = 140.dp, start = 30.dp, end = 30.dp)
            ) {

                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)

                ) {
                    OutlinedTextField(modifier = modifier.weight(1f),
                        value = firstName.value,
                        onValueChange = {
                            firstName.value = it
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
                        ),
                        singleLine = true,
                        readOnly = !isEditProfile.value,
                        label = {
                            Text(
                                text = "First Name", color = Color.Gray
                            )
                        })

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(modifier = modifier.weight(1f),
                        value = lastName.value,
                        onValueChange = {
                            lastName.value = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(20.dp),

                        readOnly = !isEditProfile.value,

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
                                text = "Last Name", color = Color.Gray
                            )
                        })


                }

                OutlinedTextField(modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(20.dp),

                    readOnly = !isEditProfile.value,

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFEBF5FC),
                        unfocusedContainerColor = Color(0xFFEBF5FC),
                        focusedIndicatorColor = Color(0xFFF68B8B),
                        unfocusedIndicatorColor = Color(0xFFF68B8B),
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Email", color = Color.Gray
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

                    readOnly = !isEditProfile.value,

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
                            text = "Password", color = Color.Gray

                        )
                    },
                )

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = phoneNumber.value,
                    onValueChange = {
                        phoneNumber.value = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color.Gray

                        )
                    },
                    shape = RoundedCornerShape(20.dp),

                    readOnly = !isEditProfile.value,

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
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
                            text = "Phone Number", color = Color.Gray

                        )
                    }
                )

                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = address.value,
                    onValueChange = {
                        address.value = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.Gray

                        )
                    },
                    shape = RoundedCornerShape(20.dp),

                    readOnly = !isEditProfile.value,

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFEBF5FC),
                        unfocusedContainerColor = Color(0xFFEBF5FC),
                        focusedIndicatorColor = Color(0xFFF68B8B),
                        unfocusedIndicatorColor = Color(0xFFF68B8B),
                    ),
                    label = {
                        Text(
                            text = "Address", color = Color.Gray

                        )
                    }
                )


                Spacer(modifier = Modifier.height(20.dp))

                if (!isEditProfile.value) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF68B8B)
                        ),

                        onClick = {
                            isEditProfile.value = true
                        },
                    ) {
                        Text(
                            text = "Edit Your Details",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                } else {

                    Button(
                        onClick = {

                            val updatedUser = UserDataModel(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                email = email.value,
                                password = password.value,
                                phoneNumber = phoneNumber.value,
                                address = address.value,
                                profilePicture = imageUri.value.ifEmpty {
                                    userDetailsState.value.isSuccess?.profilePicture ?: ""
                                }
                            )

                            if (imageUri.value.isNotEmpty() && imageUri.value != userDetailsState.value.isSuccess?.profilePicture) {
                                viewModel.addProfilePhoto(
                                    UserDataModel(
                                        profilePicture = imageUri.value
                                    )
                                )
                            } else {
                                viewModel.updateUser(updatedUser)

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
                            text = "Update Details",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF68B8B)
                        ),

                        onClick = {
                            isEditProfile.value = false
                        },
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {

                        navController.navigate(Routes.ShowAllOrdersScreen)

                    }
                    .height(50.dp)) {
                    Box(
                        modifier
                            .fillMaxSize()
                            .background(
                                Color(0xFFFF9800)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your Orders",
                            color = Color.White
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun CustomDialogLogOut(
    navController: NavController,
    openDialogCustom: MutableState<Boolean>,
    imageUri: MutableState<String> = mutableStateOf(""),
    viewModel: MyViewModel = hiltViewModel()
) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {

        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(16.dp),
            colors = cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.padding(25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(color = Color(0xFFF68B8B))
                ) {
                    AsyncImage(
                        model = R.drawable.logout,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Text(
                    text = "Do you really want to Logout \uD83E\uDD7A!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 1.dp)
                        .background(color = Color(0xFFF68B8B))
                        .clickable {
                            openDialogCustom.value = false

                        }
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 1.dp)
                        .background(color = Color(0xFFF68B8B))
                        .clickable {
                            openDialogCustom.value = false
                            viewModel.logOutUser()
                        }
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Yes",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }


        }

    }
}
