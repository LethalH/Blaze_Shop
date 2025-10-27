package com.fuchsia.blazeshopuser.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val paymentId: String = "",
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val totalPrice: String = "",
    val productImageUrl: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val city: String = "",
    val pinCode: String = "",
    val phoneNumber: String = "",
    val date: String = ""
)
