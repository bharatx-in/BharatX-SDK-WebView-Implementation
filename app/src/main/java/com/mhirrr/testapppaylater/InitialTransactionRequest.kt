package com.mhirrr.testapppaylater

import java.io.Serializable


data class InitialTransactionRequest(
    val amount: Int,
    val id: String,
    val user: UserModel,
    val redirect: Redirect
) : Serializable {
    data class UserModel(
        val name: String,
        val email: String,
        val phoneNumber: String
    ) : Serializable
    data class Redirect(
        val url: String = "https://bharatx.tech"
    )
}