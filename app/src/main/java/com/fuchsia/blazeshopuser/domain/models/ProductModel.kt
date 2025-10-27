package com.fuchsia.blazeshopuser.domain.models

data class ProductModel(
    val productId: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productPrice: String = "",
    val discountedPrice: String = "",
    val productCategory: String = "",
    val productImageUrl: String = "",
    val productQuantity: String = "",
    val productRating: String = "",
    val productSize: String = "",
    val productColor: String = "",
    val isFavourite: Boolean = false
)
