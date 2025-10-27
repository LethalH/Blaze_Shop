package com.fuchsia.blazeshopuser.data.repoImp

import android.net.Uri
import android.util.Log
import com.fuchsia.blazeshopuser.common.BANNERS_PATH
import com.fuchsia.blazeshopuser.common.CATEGORY_PATH
import com.fuchsia.blazeshopuser.common.ResultState
import com.fuchsia.blazeshopuser.common.USER_PATH
import com.fuchsia.blazeshopuser.domain.models.CartModel
import com.fuchsia.blazeshopuser.domain.models.OrderModel
import com.fuchsia.blazeshopuser.domain.models.ProductCategoryModel
import com.fuchsia.blazeshopuser.domain.models.ProductModel
import com.fuchsia.blazeshopuser.domain.models.UserDataModel
import com.fuchsia.blazeshopuser.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import androidx.core.net.toUri

class RepoImp @Inject constructor(
    val auth: FirebaseAuth,
    val fireStore: FirebaseFirestore,
    val storage: FirebaseStorage
) : Repo {
    override suspend fun registerWithEmailAndPassword(userData: UserDataModel): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            auth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {

                    fireStore.collection(USER_PATH).document(it.user!!.uid.toString()).set(userData)
                        .addOnSuccessListener {
                            auth.signOut()
                            trySend(ResultState.Success("Success"))


                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.message.toString()))

                        }


                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))

                }
            awaitClose {
                close()

            }


        }

    override suspend fun loginWithEmailAndPassword(userData: UserDataModel): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            auth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Success"))

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))

                }
            awaitClose {
                close()
            }

        }

    override suspend fun getAllCategory(): Flow<ResultState<List<ProductCategoryModel>>> =
        callbackFlow {

            trySend(ResultState.Loading)
            fireStore.collection(CATEGORY_PATH).get().addOnSuccessListener {

                val categoryList = it.toObjects(ProductCategoryModel::class.java)
                trySend(ResultState.Success(categoryList))


            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
            awaitClose {
                close()
            }


        }

    override suspend fun getBanners(): Flow<ResultState<List<ProductCategoryModel>>> =
        callbackFlow {

            trySend(ResultState.Loading)
            fireStore.collection(BANNERS_PATH).get().addOnSuccessListener {

                val bannerList = it.toObjects(ProductCategoryModel::class.java)
                trySend(ResultState.Success(bannerList))


            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun getHomeCategory(): Flow<ResultState<List<ProductCategoryModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            fireStore.collection(CATEGORY_PATH).limit(8).get().addOnSuccessListener {
                val categoryList = it.toObjects(ProductCategoryModel::class.java)
                trySend(ResultState.Success(categoryList))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))

            }
            awaitClose {
                close()
            }

        }

    override suspend fun getProductByCategory(categoryName: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            fireStore.collection("products")
                .whereEqualTo("productCategory", categoryName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val productList = querySnapshot.documents.mapNotNull { document ->
                        val product = document.toObject(ProductModel::class.java)
                        product?.copy(productId = document.id)
                    }

                    trySend(ResultState.Success(productList))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

            awaitClose { close() }
        }.flowOn(Dispatchers.IO)

    override suspend fun getHomeProductByCategory(categoryName: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            fireStore.collection("products")
                .whereEqualTo("productCategory", categoryName)
                .limit(8)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val flashSaleList = querySnapshot.documents.mapNotNull { document ->
                        val product = document.toObject(ProductModel::class.java)
                        product?.copy(productId = document.id)
                    }

                    trySend(ResultState.Success(flashSaleList))
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.message.toString()))
                }

            awaitClose { close() }
        }


    override suspend fun getUserDetails(): Flow<ResultState<UserDataModel>> = callbackFlow {
        trySend(ResultState.Loading)
        fireStore.collection(USER_PATH).document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val userData = it.toObject(UserDataModel::class.java)
                trySend(ResultState.Success(userData!!))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }


    }

    override suspend fun updateUserDetails(userData: UserDataModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            fireStore.collection(USER_PATH).document(auth.currentUser!!.uid).set(userData)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Success"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }

        }

    override suspend fun addProfilePhoto(userData: UserDataModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            storage.reference.child("User Profile Photo/${auth.currentUser!!.uid}").putFile(
                userData.profilePicture.toUri()
            ).addOnSuccessListener { it ->
                it.storage.downloadUrl.addOnSuccessListener {
                    trySend(
                        ResultState.Success(it.toString())
                    )

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))

                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))

            }
            awaitClose {
                close()
            }

        }

    override suspend fun logOut(): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)
        auth.signOut()
        trySend(ResultState.Success("Success"))
        awaitClose {
            close()
        }
    }

    override suspend fun getProductDetails(productId: String): Flow<ResultState<ProductModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            Log.d("TAG", "getProductDetails: $productId")

            if (productId.isBlank()) {
                trySend(ResultState.Error("Invalid product ID"))
                close()
                return@callbackFlow
            }
            fireStore.collection("products").document(productId).get().addOnSuccessListener {
                val product = it.toObject(ProductModel::class.java)
                trySend(ResultState.Success(product!!))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun addToFavourite(favModel: ProductModel): Flow<ResultState<ProductModel>> =
        callbackFlow {

            trySend(ResultState.Loading)
            fireStore.collection("Users")
                .document(auth.currentUser!!.uid)
                .collection("Favourite")
                .document(
                    favModel.productId
                ).set(favModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success(favModel))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

            awaitClose {
                close()
            }

        }

    override suspend fun getFavourite(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        fireStore.collection("Users").document(auth.currentUser!!.uid).collection("Favourite").get()
            .addOnSuccessListener {
                val favList = it.toObjects(ProductModel::class.java)
                trySend(ResultState.Success(favList))
                Log.d("Favourite", favList.toString())
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }

        awaitClose {
            close()
        }

    }

    override suspend fun addToCart(cartModel: CartModel): Flow<ResultState<CartModel>> =
        callbackFlow {

            trySend(ResultState.Loading)

            fireStore.collection("Users")
                .document(auth.currentUser!!.uid)
                .collection("Cart Items")
                .document(
                    cartModel.productId
                ).set(cartModel)
                .addOnSuccessListener {
                    trySend(ResultState.Success(cartModel))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

            awaitClose {
                close()
            }
        }

    override suspend fun getCart(): Flow<ResultState<List<CartModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        fireStore.collection("Users").document(auth.currentUser!!.uid).collection("Cart Items")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val cartList = querySnapshot.documents.mapNotNull { document ->
                    val product = document.toObject(CartModel::class.java)
                    product?.copy(productId = document.id)
                }
                trySend(ResultState.Success(cartList))

                Log.d("Cart", cartList.toString())

            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }

    override suspend fun deleteFav(productId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        fireStore.collection("Users").document(auth.currentUser!!.uid).collection("Favourite")
            .document(productId).delete()
            .addOnSuccessListener {
                trySend(ResultState.Success("Success"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
            close()
        }

    }

    override suspend fun deleteCart(productId: String): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)
        fireStore.collection("Users").document(auth.currentUser!!.uid).collection("Cart Items")
            .document(productId).delete()
            .addOnSuccessListener {
                trySend(ResultState.Success("Success"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        awaitClose {
        }
    }


    override suspend fun searchProducts(query: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            fireStore.collection("Products")
                .orderBy("productName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val searchList = querySnapshot.documents.mapNotNull { document ->
                        val product = document.toObject(ProductModel::class.java)
                        product?.copy(productId = document.id)
                    }
                    trySend(ResultState.Success(searchList))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

            awaitClose {
                close()
            }

        }

    override suspend fun createOrder(orderModel: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        Log.d("createOrder", "Order JSON: $orderModel")

        try {
            val order = Json.decodeFromString<OrderModel>(orderModel)

            val orderMap = mapOf(
                "paymentId" to order.paymentId,
                "userId" to order.userId,
                "productId" to order.productId,
                "productName" to order.productName,
                "totalPrice" to order.totalPrice,
                "productImageUrl" to order.productImageUrl,
                "email" to order.email,
                "firstName" to order.firstName,
                "lastName" to order.lastName,
                "address" to order.address,
                "city" to order.city,
                "pinCode" to order.pinCode,
                "phoneNumber" to order.phoneNumber,
                "date" to order.date
            )

            val currentUser = auth.currentUser
            if (currentUser == null) {
                trySendBlocking(ResultState.Error("User is not authenticated"))
                close()
                return@callbackFlow
            }

            val orderId = UUID.randomUUID().toString()

            fireStore.collection("Users")
                .document(currentUser.uid)
                .collection("Orders")
                .document(orderId)
                .set(orderMap)
                .addOnSuccessListener {
                    trySendBlocking(ResultState.Success("Order created successfully"))
                    close()
                }
                .addOnFailureListener { e ->
                    trySendBlocking(ResultState.Error("Failed to create order: ${e.message}"))
                    Log.e("createOrder", "Error uploading order", e)
                    close()
                }
        } catch (e: Exception) {
            trySendBlocking(ResultState.Error("Error during order creation: ${e.message}"))
            Log.e("createOrder", "Exception during order creation", e)
            close()
        }

        awaitClose()
    }

    override suspend fun getOrders(): Flow<ResultState<List<OrderModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        fireStore.collection("Users").document(auth.currentUser!!.uid).collection("Orders").get()
            .addOnSuccessListener {
                val orderList = it.toObjects(OrderModel::class.java)
                trySend(ResultState.Success(orderList))

            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }

        awaitClose {
            close()
        }

    }


}