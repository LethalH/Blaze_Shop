package com.fuchsia.blazeshopuser.domain.models

data class UserDataModel(
    val firstName: String ="",
    val lastName: String ="",
    val email: String ="",
    val password: String ="",
    val phoneNumber: String ="",
    val address: String ="",
    val profilePicture: String =""
)
