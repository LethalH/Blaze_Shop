package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetHomeCategoryUseCase @Inject constructor(private val repo: Repo)  {
    suspend fun getHomeCategory() = repo.getHomeCategory()

}