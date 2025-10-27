package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.CartModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(val repo: Repo) {
    suspend fun addToCartUseCase(cartModel: CartModel) = repo.addToCart(cartModel)

}