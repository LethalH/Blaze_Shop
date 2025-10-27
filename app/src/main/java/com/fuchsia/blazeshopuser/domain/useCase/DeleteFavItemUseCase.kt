package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class DeleteFavItemUseCase @Inject constructor(val repo: Repo) {
    suspend fun deleteFavItemUseCase(productId: String) = repo.deleteFav(productId)

}