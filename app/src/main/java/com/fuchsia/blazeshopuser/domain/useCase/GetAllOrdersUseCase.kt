package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(val repo: Repo)  {
    suspend fun getAllOrdersUseCase() = repo.getOrders()

}