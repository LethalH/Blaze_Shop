package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetCartItemUseCase @Inject constructor(val repo: Repo) {
    suspend fun getCartItemUseCase() = repo.getCart()
}