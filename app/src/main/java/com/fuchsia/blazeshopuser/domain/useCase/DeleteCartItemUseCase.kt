package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(val repo: Repo) {
    suspend fun deleteCartItemUseCase(productId: String) = repo.deleteCart(productId)
}