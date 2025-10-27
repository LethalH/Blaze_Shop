package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetFavListUseCase @Inject constructor(val repo: Repo) {
    suspend fun getFavourite() = repo.getFavourite()


}