package com.fuchsia.blazeshopuser.domain.models

data class CartModel(
    val productId: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productPrice: String = "",
    val discountedPrice: String = "",
    val totalPrice: String = "",
    val productCategory: String = "",
    val productImageUrl: String = "",
    val productCount: String = "",
    val productSize: String = "",
    val productColor: String = "",
)
