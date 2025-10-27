package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetBannersUseCase @Inject constructor(val repo: Repo) {
    suspend fun getBannersUseCase() = repo.getBanners()

}