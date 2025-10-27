package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject


class CreateUserUseCase @Inject constructor(private val repo: Repo) {
    suspend fun createUserUseCase(userData: UserDataModel) = repo.registerWithEmailAndPassword(userData)
}