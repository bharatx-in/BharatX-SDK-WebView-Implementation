package com.mhirrr.testapppaylater

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofitInstance: Retrofit = Retrofit.Builder()
            .baseUrl("https://web.bharatx.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        val bharatXApiInstance = retrofitInstance.create(RetrofitApi::class.java)
    }

}