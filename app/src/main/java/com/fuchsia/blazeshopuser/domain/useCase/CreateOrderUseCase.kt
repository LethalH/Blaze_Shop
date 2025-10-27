package com.fuchsia.blazeshopuser.domain.useCase

import com.fuchsia.blazeshopuser.domain.repo.Repo
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(val repo: Repo) {
    suspend fun createOrderUseCase(OrderModel: String) = repo.createOrder(OrderModel)


}