package com.fuchsia.blazeshopuser.domain.repo

import com.fuchsia.blazeshopuser.common.ResultState
import com.fuchsia.blazeshopuser.domain.models.CartModel
import com.fuchsia.blazeshopuser.domain.models.OrderModel
import com.fuchsia.blazeshopuser.domain.models.ProductCategoryModel
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import kotlinx.coroutines.flow.Flow

interface Repo {

    suspend fun registerWithEmailAndPassword(userData: UserDataModel) : Flow<ResultState<String>>

    suspend fun loginWithEmailAndPassword(userData: UserDataModel) : Flow<ResultState<String>>

    suspend fun getAllCategory() : Flow<ResultState<List<ProductCategoryModel>>>

    suspend fun getBanners() : Flow<ResultState<List<ProductCategoryModel>>>

    suspend fun getProductByCategory(categoryName: String) : Flow<ResultState<List<ProductModel>>>

    suspend fun getHomeCategory() : Flow<ResultState<List<ProductCategoryModel>>>

    suspend fun getHomeProductByCategory(categoryName: String) : Flow<ResultState<List<ProductModel>>>

    suspend fun getUserDetails(): Flow<ResultState<UserDataModel>>

    suspend fun updateUserDetails(userData: UserDataModel): Flow<ResultState<String>>

    suspend fun addProfilePhoto(userData: UserDataModel): Flow<ResultState<String>>

    suspend fun logOut(): Flow<ResultState<String>>

    suspend fun getProductDetails(productId: String): Flow<ResultState<ProductModel>>

    suspend fun addToFavourite(favModel: ProductModel): Flow<ResultState<ProductModel>>

    suspend fun getFavourite(): Flow<ResultState<List<ProductModel>>>

    suspend fun addToCart(cartModel: CartModel): Flow<ResultState<CartModel>>

    suspend fun getCart(): Flow<ResultState<List<CartModel>>>

    suspend fun deleteFav(productId: String): Flow<ResultState<String>>

    suspend fun deleteCart(productId: String): Flow<ResultState<String>>

    suspend fun searchProducts(query: String): Flow<ResultState<List<ProductModel>>>

    suspend fun createOrder(orderModel: String): Flow<ResultState<String>>

    suspend fun getOrders(): Flow<ResultState<List<OrderModel>>>
}