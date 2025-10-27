package com.fuchsia.blazeshopuser.presentation.nav

import kotlinx.serialization.Serializable

sealed class Routes {


    @Serializable
    data object CartScreen : Routes()

    @Serializable
    data object BottomNavBar  : Routes()

    @Serializable
    data object LogInScreen : Routes()

    @Serializable
    data object SignUpScreen : Routes()

    @Serializable
    data object NotificationScreen : Routes()

    @Serializable
    data object PaymentSuccessScreen: Routes()

    @Serializable
    data class ProductDetailsScreen (val productId: String): Routes()

    @Serializable
    data object AllCategoryScreen : Routes()

    @Serializable
    data class ProductByCategoryScreen(val categoryName: String) : Routes()

    @Serializable
    data class RazorPayScreen(
        val productId: String,
        val productImageUrl: String,
        val productName: String,
        val totalPrice: String,
        val productCount: String,
        val email: String,
        val firstName: String,
        val lastName: String,
        val address: String,
        val city: String,
        val pinCode: String,
        val phoneNumber: String
    ) : Routes()

    @Serializable
    data class SuccessfulRegScreen(val regSuccess: String) : Routes()

    @Serializable
    data object UserProfileScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object FavouriteScreen : Routes()

    @Serializable
    data object SearchProductScreen : Routes()

    @Serializable
    data class  PaymentDetailsScreen(
        val productId: String,
        val productImageUrl: String,
        val productName: String,
        val totalPrice: String,
        val productCount: String
    ) : Routes()

    @Serializable
    data object ShowAllOrdersScreen : Routes()


}