package com.fuchsia.blazeshopuser.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuchsia.blazeshopuser.common.ResultState
import com.fuchsia.blazeshopuser.domain.models.CartModel
import com.fuchsia.blazeshopuser.domain.models.OrderModel
import com.fuchsia.blazeshopuser.domain.models.ProductCategoryModel
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.useCase.AddProfilePhotoUseCase
import com.fuchsia.blazeshopuser.domain.useCase.AddToCartUseCase
import com.fuchsia.blazeshopuser.domain.useCase.AddToFavouriteUseCase
import com.fuchsia.blazeshopuser.domain.useCase.CreateOrderUseCase
import com.fuchsia.blazeshopuser.domain.useCase.CreateUserUseCase
import com.fuchsia.blazeshopuser.domain.useCase.DeleteCartItemUseCase
import com.fuchsia.blazeshopuser.domain.useCase.DeleteFavItemUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetAllCategoryUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetAllOrdersUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetBannersUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetCartItemUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetFavListUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetHomeCategoryUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetHomeProductByCategoryUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetProductByCategoryUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetProductDetailsUseCase
import com.fuchsia.blazeshopuser.domain.useCase.GetUserDetailsUseCase
import com.fuchsia.blazeshopuser.domain.useCase.LogInUseCase
import com.fuchsia.blazeshopuser.domain.useCase.LogOutUseCase
import com.fuchsia.blazeshopuser.domain.useCase.SearchProductUseCase
import com.fuchsia.blazeshopuser.domain.useCase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val logInUseCase: LogInUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getProductByCategoryUseCase: GetProductByCategoryUseCase,
    private val getHomeProductByCategoryUseCase: GetHomeProductByCategoryUseCase,
    private val getHomeCategoryUseCase: GetHomeCategoryUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val addProfilePhotoUseCase: AddProfilePhotoUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val getFavListUseCase: GetFavListUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartItemUseCase,
    private val deleteFavItemUseCase: DeleteFavItemUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val getBannersUseCase: GetBannersUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase

) : ViewModel() {

    private val _createUserState = MutableStateFlow(CreateUserState())
    val createUserState = _createUserState.asStateFlow()

    fun createUser(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.createUserUseCase(userData).collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _createUserState.value = CreateUserState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _createUserState.value = CreateUserState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _createUserState.value = CreateUserState(isSuccess = result.data)

                    }
                }

            }

        }

    }

    private val _logInState = MutableStateFlow(LogInState())
    val logInState = _logInState.asStateFlow()

    fun logInUser(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {

            logInUseCase.logInUseCase(userData).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _logInState.value = LogInState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _logInState.value = LogInState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _logInState.value = LogInState(isSuccess = result.data)

                    }
                }

            }

        }

    }


    private val _getAllCategoryState = MutableStateFlow(GetAllCategoryState())
    val getAllCategoryState = _getAllCategoryState.asStateFlow()

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCategoryUseCase.getAllCategoryUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _getAllCategoryState.value = GetAllCategoryState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getAllCategoryState.value = GetAllCategoryState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getAllCategoryState.value = GetAllCategoryState(isSuccess = result.data)

                    }
                }
            }

        }
    }

    private val _getHomeCategoryState = MutableStateFlow(GetAllCategoryState())
    val getHomeCategoryState = _getHomeCategoryState.asStateFlow()

    fun getHomeCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeCategoryUseCase.getHomeCategory().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _getHomeCategoryState.value = GetAllCategoryState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getHomeCategoryState.value = GetAllCategoryState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getHomeCategoryState.value = GetAllCategoryState(isSuccess = result.data)
                    }


                }
            }


        }
    }

    private val _getProductByCategoryState = MutableStateFlow(GetProductState())
    val getProductByCategoryState = _getProductByCategoryState.asStateFlow()

    fun getProductByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductByCategoryUseCase.getProductByCategoryUseCase(categoryName)
                .collect { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            _getProductByCategoryState.value = GetProductState(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getProductByCategoryState.value =
                                GetProductState(error = result.message)
                        }

                        is ResultState.Success -> {
                            _getProductByCategoryState.value =
                                GetProductState(isSuccess = result.data)
                        }
                    }
                }


        }

    }

    private val _getHomeProductByCategoryState = MutableStateFlow(GetProductState())
    val getHomeProductByCategoryState = _getHomeProductByCategoryState.asStateFlow()

    fun getHomeFlashSale(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeProductByCategoryUseCase.getHomeProductByCategoryUseCase(categoryName)
                .collect { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            _getHomeProductByCategoryState.value = GetProductState(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _getHomeProductByCategoryState.value =
                                GetProductState(error = result.message)
                        }

                        is ResultState.Success -> {
                            _getHomeProductByCategoryState.value =
                                GetProductState(isSuccess = result.data)
                        }
                    }
                }

        }
    }

    private val _userDetailsState = MutableStateFlow(UserDetailsState())
    val userDetailsState = _userDetailsState.asStateFlow()

    fun getUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {

            getUserDetailsUseCase.getUserDetailsUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _userDetailsState.value = UserDetailsState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _userDetailsState.value = UserDetailsState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _userDetailsState.value = UserDetailsState(isSuccess = result.data)

                    }

                }

            }

        }
    }

    private val _addProfilePhotoState = MutableStateFlow(AddProfilePhotoState())
    val addProfilePhotoState = _addProfilePhotoState.asStateFlow()

    fun addProfilePhoto(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                addProfilePhotoUseCase.addProfilePhotoUseCase(userData).collect { result ->
                    when (result) {

                        is ResultState.Loading -> {
                            _addProfilePhotoState.value = AddProfilePhotoState(isLoading = true)
                        }

                        is ResultState.Error -> {
                            _addProfilePhotoState.value =
                                AddProfilePhotoState(error = result.message)
                        }

                        is ResultState.Success -> {
                            _addProfilePhotoState.value =
                                AddProfilePhotoState(isSuccess = result.data)
                            Log.d("TAG", "addProfilePhoto: ${result.data}")

                        }

                    }

                }

            }

        }

    }

    private val _logOutState = MutableStateFlow(LogInState())
    val logOutState = _logOutState.asStateFlow()
    fun logOutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            logOutUseCase.logOutUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _logOutState.value = LogInState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _logOutState.value = LogInState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _logOutState.value = LogInState(isSuccess = result.data)
                    }
                }

            }


        }


    }

    private val _updateUserState = MutableStateFlow(CreateUserState())
    val updateUserState = _updateUserState.asStateFlow()

    fun updateUser(userData: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase.updateUserUseCase(userData).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _updateUserState.value = CreateUserState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _updateUserState.value = CreateUserState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _updateUserState.value = CreateUserState(isSuccess = result.data)
                    }

                }

            }
        }

    }

    fun resetUpdateUserState() {
        _updateUserState.value = CreateUserState() // Reset to default empty state
    }
    fun resetCreateUserState() {
        _createUserState.value = CreateUserState()
    }
    private val _getProductDetailsState = MutableStateFlow(GetProductDetailsState())
    val getProductDetailsState = _getProductDetailsState.asStateFlow()

    fun getProductDetails(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductDetailsUseCase.getProductDetailsUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _getProductDetailsState.value = GetProductDetailsState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getProductDetailsState.value =
                            GetProductDetailsState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getProductDetailsState.value =
                            GetProductDetailsState(isSuccess = result.data)
                    }

                }

            }

        }
    }

    private val _addToFavouriteState = MutableStateFlow(AddToFavouriteState())
    val addToFavouriteState = _addToFavouriteState.asStateFlow()
    fun addToFavourite(favModel: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addToFavouriteUseCase.addToFavouriteUseCase(favModel).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _addToFavouriteState.value = AddToFavouriteState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _addToFavouriteState.value = AddToFavouriteState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _addToFavouriteState.value =
                            AddToFavouriteState(isSuccess = result.data.toString())
                    }
                }

            }
        }
    }


    private val _getFavListState = MutableStateFlow(GetProductState())
    val getFavListState = _getFavListState.asStateFlow()

    fun getFavList() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavListUseCase.getFavourite().collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _getFavListState.value = GetProductState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getFavListState.value = GetProductState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getFavListState.value = GetProductState(isSuccess = result.data)
                    }
                }

            }

        }
    }

    private val _addToCartState = MutableStateFlow(AddToFavouriteState())
    val addToCartState = _addToCartState.asStateFlow()

    fun addToCart(cartModel: CartModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.addToCartUseCase(cartModel).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _addToCartState.value = AddToFavouriteState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _addToCartState.value = AddToFavouriteState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _addToCartState.value =
                            AddToFavouriteState(isSuccess = result.data.toString())
                    }
                }
            }

        }

    }

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()
    fun getCart() {
        viewModelScope.launch(Dispatchers.IO) {
            getCartUseCase.getCartItemUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _getCartState.value = GetCartState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getCartState.value = GetCartState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getCartState.value = GetCartState(isSuccess = result.data)
                    }
                }
            }

        }
    }

    private val _deleteCartState = MutableStateFlow(AddToFavouriteState())
    val deleteCartState = _deleteCartState.asStateFlow()
    fun deleteCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCartItemUseCase.deleteCartItemUseCase(productId).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _deleteCartState.value = AddToFavouriteState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _deleteCartState.value = AddToFavouriteState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _deleteCartState.value =
                            AddToFavouriteState(isSuccess = result.data.toString())

                    }

                }


            }

        }
    }

    private val _deleteFavState = MutableStateFlow(AddToFavouriteState())
    val deleteFavState = _deleteFavState.asStateFlow()

    fun deleteFav(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFavItemUseCase.deleteFavItemUseCase(productId).collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _deleteFavState.value = AddToFavouriteState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _deleteFavState.value = AddToFavouriteState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _deleteFavState.value =
                            AddToFavouriteState(isSuccess = result.data.toString())
                    }
                }
            }
        }

    }

    private val _getBannerState = MutableStateFlow(GetAllCategoryState())
    val getBannerState = _getBannerState.asStateFlow()

    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            getBannersUseCase.getBannersUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _getBannerState.value = GetAllCategoryState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getBannerState.value = GetAllCategoryState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getBannerState.value = GetAllCategoryState(isSuccess = result.data)
                    }
                }
            }
        }

    }


    private val _searchProductState = MutableStateFlow(GetProductState())
    val searchProductState = _searchProductState.asStateFlow()
    fun searchProduct(query: String) {

        viewModelScope.launch(Dispatchers.IO) {
            searchProductUseCase.searchProductUseCase(query).collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _searchProductState.value = GetProductState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _searchProductState.value = GetProductState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _searchProductState.value = GetProductState(isSuccess = result.data)
                    }
                }

            }

        }
    }

    private val _createOrderState = MutableStateFlow(GetOrderDetailsState())
    val createOrderState = _createOrderState.asStateFlow()

    fun createOrder(orderProductModel: String) {

        Log.d("TAG", "createOrder: $orderProductModel")
        viewModelScope.launch(Dispatchers.IO) {
            createOrderUseCase.createOrderUseCase(orderProductModel).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _createOrderState.value = GetOrderDetailsState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _createOrderState.value = GetOrderDetailsState(error = result.message)
                        Log.d("TAG", "createOrder: ${result.message}")
                    }

                    is ResultState.Success -> {
                        _createOrderState.value = GetOrderDetailsState(isSuccess = true)
                    }

                }


            }
        }

    }

    private val _getOrdersState = MutableStateFlow(GetAllOrderState())
    val getOrdersState = _getOrdersState.asStateFlow()

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllOrdersUseCase.getAllOrdersUseCase().collect { result ->

                when (result) {
                    is ResultState.Loading -> {
                        _getOrdersState.value = GetAllOrderState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _getOrdersState.value = GetAllOrderState(error = result.message)
                    }

                    is ResultState.Success -> {
                        _getOrdersState.value = GetAllOrderState(isSuccess = result.data)
                    }
                }
            }

        }
    }

}

data class GetAllOrderState(
    var isLoading: Boolean = false,
    var isSuccess: List<OrderModel>? = null,
    var error: String? = null
)

data class GetOrderDetailsState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean? = null,
    var error: String? = null

)

data class AddToFavouriteState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val error: String? = null
)

class GetAllCategoryState(
    val isLoading: Boolean = false,
    val isSuccess: List<ProductCategoryModel>? = null,
    val error: String? = null
)

data class GetProductState(
    val isLoading: Boolean = false,
    val isSuccess: List<ProductModel>? = null,
    val error: String? = null
)

data class GetCartState(
    val isLoading: Boolean = false,
    val isSuccess: List<CartModel>? = null,
    val error: String? = null
)

data class GetProductDetailsState(
    val isLoading: Boolean = false,
    val isSuccess: ProductModel? = null,
    val error: String? = null
)

data class CreateUserState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val error: String? = null

)

data class LogInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val error: String? = null
)

data class UserDetailsState(
    val isLoading: Boolean = false,
    val isSuccess: UserDataModel? = null,
    val error: String? = null
)

data class AddProfilePhotoState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val error: String? = null
)
