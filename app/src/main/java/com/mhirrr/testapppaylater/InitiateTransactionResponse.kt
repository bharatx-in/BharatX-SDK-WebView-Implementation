package com.mhirrr.testapppaylater

import java.io.Serializable

data class InitiateTransactionResponse(
    val message: String,
    val redirectUrl: String
): Serializable