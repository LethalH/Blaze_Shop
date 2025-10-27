package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val repo: Repo) {

    suspend fun logInUseCase(userData: UserDataModel) = repo.loginWithEmailAndPassword(userData)

}