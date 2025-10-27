package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetProductByCategoryUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getProductByCategoryUseCase(categoryName: String) = repo.getProductByCategory(categoryName)
}