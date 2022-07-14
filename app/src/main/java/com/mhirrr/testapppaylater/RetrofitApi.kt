package com.mhirrr.testapppaylater

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface RetrofitApi {

    @POST("/api/transaction")
    suspend fun initiateTransaction(
        @Body initiateTransactionRequest: InitialTransactionRequest,
        @HeaderMap initiateTransactionHeaders: HashMap<String, String>
    ): InitiateTransactionResponse
}