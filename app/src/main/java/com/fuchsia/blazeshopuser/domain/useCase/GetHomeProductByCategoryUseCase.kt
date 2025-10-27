package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetHomeProductByCategoryUseCase @Inject constructor(private val repo: Repo)  {
    suspend fun getHomeProductByCategoryUseCase(categoryName: String) = repo.getHomeProductByCategory(categoryName)

}

