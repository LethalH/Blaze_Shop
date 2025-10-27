package com.fuchsia.blazeshopuser.presentation.nav

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fuchsia.blazeshopuser.domain.models.OrderModel
import com.fuchsia.blazeshopuser.presentation.screens.AllCategoryScreen
import com.fuchsia.blazeshopuser.presentation.screens.BottomNavBar
import com.fuchsia.blazeshopuser.presentation.screens.CartScreen
import com.fuchsia.blazeshopuser.presentation.screens.FavouriteScreen
import com.fuchsia.blazeshopuser.presentation.screens.HomeScreen
import com.fuchsia.blazeshopuser.presentation.screens.LogInScreen
import com.fuchsia.blazeshopuser.presentation.screens.NotificationScreen
import com.fuchsia.blazeshopuser.presentation.screens.PaymentDetailsScreen
import com.fuchsia.blazeshopuser.presentation.screens.PaymentSuccessScreen
import com.fuchsia.blazeshopuser.presentation.screens.ProductByCategoryScreen
import com.fuchsia.blazeshopuser.presentation.screens.ProductDetailsScreen
import com.fuchsia.blazeshopuser.presentation.screens.RazorPayScreen
import com.fuchsia.blazeshopuser.presentation.screens.SearchProductScreen
import com.fuchsia.blazeshopuser.presentation.screens.ShowAllOrdersScreen
import com.fuchsia.blazeshopuser.presentation.screens.SignUpScreen
import com.fuchsia.blazeshopuser.presentation.screens.SuccessfulRegScreen
import com.fuchsia.blazeshopuser.presentation.screens.UserProfileScreen
import com.fuchsia.blazeshopuser.presentation.viewModel.GetOrderDetailsState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.json.Json

@Composable
fun App(
    modifier: Modifier,
    firebaseAuth: FirebaseAuth,
    createOrderState: State<GetOrderDetailsState>,
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (firebaseAuth.currentUser != null) {
            Routes.SuccessfulRegScreen(
                regSuccess = "0"
            )
        } else {
            Routes.LogInScreen
        }
    ) {

        composable<Routes.LogInScreen> {
            LogInScreen(navController = navController)
        }

        composable<Routes.SignUpScreen> {
            SignUpScreen(
                navController = navController,
            )
        }

        composable<Routes.BottomNavBar> {
            BottomNavBar(
                navController = navController
            )
        }

        composable<Routes.NotificationScreen> {
            NotificationScreen(
                navController = navController
            )
        }


        composable<Routes.PaymentSuccessScreen> { it ->

            val jsonOrder = it.arguments?.getString("orderDetails")
            val orderDetails = jsonOrder?.let { Json.decodeFromString<OrderModel>(it) }

            PaymentSuccessScreen(
                navController = navController
            )
        }

        composable<Routes.ProductDetailsScreen> {
            val productId = it.arguments?.getString("productId")
            ProductDetailsScreen(
                productId = productId!!,
                navController = navController
            )

        }

        composable<Routes.AllCategoryScreen> {
            AllCategoryScreen(
                navController = navController
            )
        }

        composable<Routes.ProductByCategoryScreen> {
            val categoryName = it.arguments?.getString("categoryName")!!

            ProductByCategoryScreen(
                categoryName = categoryName,
                navController = navController

            )
        }

        composable<Routes.RazorPayScreen> {
            val productId = it.arguments?.getString("productId")
            val productImageUrl = it.arguments?.getString("productImageUrl")
            val productName = it.arguments?.getString("productName")
            val totalPrice = it.arguments?.getString("totalPrice")
            val productCount = it.arguments?.getString("productCount")
            val email = it.arguments?.getString("email")
            val firstName = it.arguments?.getString("firstName")
            val lastName = it.arguments?.getString("lastName")
            val address = it.arguments?.getString("address")
            val city = it.arguments?.getString("city")
            val pinCode = it.arguments?.getString("pinCode")
            val phoneNumber = it.arguments?.getString("phoneNumber")

            RazorPayScreen(
                productId = productId!!,
                productImageUrl = productImageUrl!!,
                productName = productName!!,
                totalPrice = totalPrice!!,
                productCount = productCount!!,
                email = email!!,
                firstName = firstName!!,
                lastName = lastName!!,
                address = address!!,
                city = city!!,
                pinCode = pinCode!!,
                phoneNumber = phoneNumber!!,
                navController = navController,
                firebaseAuth = firebaseAuth,
            )
        }

        composable<Routes.SuccessfulRegScreen> {
            val regSuccess = it.arguments?.getString("regSuccess")
            SuccessfulRegScreen(
                regSuccess = regSuccess!!,
                navController = navController
            )
        }

        composable<Routes.UserProfileScreen> {
            UserProfileScreen(
                navController = navController
            )

        }
        composable<Routes.CartScreen> {
            CartScreen(
                navController = navController
            )

        }

        composable<Routes.HomeScreen> {
            HomeScreen(
                navController = navController
            )
        }

        composable<Routes.FavouriteScreen> {
            FavouriteScreen(
                navController = navController
            )
        }

        composable<Routes.SearchProductScreen> {
            SearchProductScreen(
                navController = navController
            )

        }

        composable<Routes.PaymentDetailsScreen> {
            val productId = it.arguments?.getString("productId")
            val productImageUrl = it.arguments?.getString("productImageUrl")
            val productName = it.arguments?.getString("productName")
            val totalPrice = it.arguments?.getString("totalPrice")
            val productCount = it.arguments?.getString("productCount")
            PaymentDetailsScreen(
                productId = productId!!,
                productImageUrl = productImageUrl!!,
                productName = productName!!,
                totalPrice = totalPrice!!,
                productCount = productCount!!,
                navController = navController
            )
        }

        composable<Routes.ShowAllOrdersScreen> {
            ShowAllOrdersScreen(
                navController = navController
            )
        }
    }
    val context = LocalContext.current

    when{
        createOrderState.value.isLoading -> {}

        createOrderState.value.error?.isNotEmpty() == true -> {
            Toast.makeText(context, createOrderState.value.error, Toast.LENGTH_LONG).show()
        }
        createOrderState.value.isSuccess == true -> {
            navController.navigate(Routes.PaymentSuccessScreen)
        }
    }

}