package com.fuchsia.blazeshopuser.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.fuchsia.blazeshopuser.R
import com.fuchsia.blazeshopuser.presentation.nav.Routes
import kotlinx.coroutines.delay

@Composable
fun SuccessfulRegScreen(
    regSuccess: String,
    navController: NavController,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
) {

    val openDialog = remember { mutableStateOf(false) }

    val showWelcome = remember { mutableStateOf(false) }


    if (regSuccess == "1") {
        openDialog.value = true

    } else {
        showWelcome.value = true

    }

    if (showWelcome.value) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(150.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.welcome),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
        LaunchedEffect(key1 = showWelcome.value) {
            delay(500)
            showWelcome.value = false
            navController.navigate(Routes.BottomNavBar)
        }
    }

    if (openDialog.value) {
        CustomDialog(
            navController = navController,
            openDialog
        )
    }
}

@Composable
fun CustomDialog(
    navController: NavController,
    openDialogCustom: MutableState<Boolean>
) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        CustomDialogUI(
            navController = navController,
            openDialogCustom
        )
    }
}

@Composable
fun CustomDialogUI(
    navController: NavController,
    openDialogCustom: MutableState<Boolean>
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White)
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
                Image(
                    painter = painterResource(id = R.drawable.tik),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = "Congratulations, you have completed your registration!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color(0xFFF68B8B))
                .clickable {
                    openDialogCustom.value = false
                    navController.popBackStack()
                    navController.navigate(Routes.LogInScreen)

                }
                .height(48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Done",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

    }
}