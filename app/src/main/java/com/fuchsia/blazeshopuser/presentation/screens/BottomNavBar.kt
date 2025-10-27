package com.fuchsia.blazeshopuser.presentation.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fuchsia.blazeshopuser.R

@Composable
fun BottomNavBar(modifier: Modifier = Modifier, navController: NavController) {

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }


    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler {
        if (backPressedOnce) {
            (context as? ComponentActivity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold (
        modifier.fillMaxSize().systemBarsPadding(),

        bottomBar = {

            NavigationBar (modifier = modifier.height(65.dp)){
                NavigationBarItem(
                    selected = selectedIndex ==0,
                    onClick = { selectedIndex = 0 },
                    icon = {
                        Icon(
                            modifier = modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = null
                        )
                    },
                    label = { /*TODO*/ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        selectedTextColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        indicatorColor = androidx.compose.ui.graphics.Color.White,

                    )


                )

                NavigationBarItem(
                    selected = selectedIndex ==1,
                    onClick = { selectedIndex = 1 },
                    icon = {
                        Icon(
                            modifier = modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.cart),
                            contentDescription = null
                        )
                    },
                    label = { /*TODO*/ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        selectedTextColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        indicatorColor = androidx.compose.ui.graphics.Color.White,


                        )


                )

                NavigationBarItem(
                    selected = selectedIndex ==2,
                    onClick = { selectedIndex = 2 },
                    icon = {
                        Icon(
                            modifier = modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = null
                        )
                    },
                    label = { /*TODO*/ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        selectedTextColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        indicatorColor = androidx.compose.ui.graphics.Color.White,


                        )


                )

                NavigationBarItem(
                    selected = selectedIndex ==3,
                    onClick = { selectedIndex = 3 },
                    icon = {
                        Icon(
                            modifier = modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = null
                        )
                    },
                    label = { /*TODO*/ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        selectedTextColor = androidx.compose.ui.graphics.Color(0xFFF68B8B),
                        indicatorColor = androidx.compose.ui.graphics.Color.White,


                        )


                )
            }


        }

    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> HomeScreen(
                    navController = navController
                )
                1 -> CartScreen(
                    navController = navController
                )
                2 -> FavouriteScreen(
                    navController = navController
                )
                3 -> UserProfileScreen(
                    navController = navController
                )
            }
        }

    }

}