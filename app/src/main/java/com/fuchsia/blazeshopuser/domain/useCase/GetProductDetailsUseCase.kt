package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(val repo: Repo) {

    suspend fun getProductDetailsUseCase(productId: String) = repo.getProductDetails(productId)

}