package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(val repo: Repo) {
    suspend fun addToFavouriteUseCase(favModel: ProductModel) = repo.addToFavourite(favModel)

}