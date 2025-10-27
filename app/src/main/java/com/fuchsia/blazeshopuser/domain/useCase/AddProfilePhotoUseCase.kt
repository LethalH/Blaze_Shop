package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class AddProfilePhotoUseCase @Inject constructor(val repo: Repo) {
    suspend fun addProfilePhotoUseCase(userData: UserDataModel) = repo.addProfilePhoto(userData)

}