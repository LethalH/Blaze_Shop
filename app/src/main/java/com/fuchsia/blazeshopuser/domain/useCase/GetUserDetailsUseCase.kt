package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(val repo: Repo) {
    suspend fun getUserDetailsUseCase() = repo.getUserDetails()
}