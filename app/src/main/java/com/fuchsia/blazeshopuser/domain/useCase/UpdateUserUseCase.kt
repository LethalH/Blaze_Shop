package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(val repo: Repo) {
    suspend fun updateUserUseCase(userData: UserDataModel) = repo.updateUserDetails(userData)

}