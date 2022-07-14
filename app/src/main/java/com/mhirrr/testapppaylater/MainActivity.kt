package com.mhirrr.testapppaylater

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mhirrr.testapppaylater.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Integer.parseInt
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transactionSubmit.setOnClickListener {
            sendRequest(
                (0..10).random().toString(),
                parseInt(binding.transactionAmount.text.toString()),
                binding.transactionName.text.toString(),
                binding.transactionEmail.text.toString(),
                binding.transactionPhone.text.toString()
            )
        }
    }

    private fun getTransactionId(): String {
        return List(16) {
            (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
        }.joinToString("")
    }

    private fun sendRequest(
        transactionId: String,
        amount: Int,
        name: String,
        email: String,
        phoneNumber: String
    ) {

        val privateApiKey = "testPrivateKey"
        val privatePartnerId = "testPartnerId"

        val requestBody = InitialTransactionRequest(
            amount,
            transactionId,
            InitialTransactionRequest.UserModel(
                name,
                email,
                "+91${phoneNumber}"
            ),
            InitialTransactionRequest.Redirect()
        )

//        val transactionInitiateBody = JSONObject()
//        transactionInitiateBody.put("amount", amount)
//        transactionInitiateBody.put("transactionId", transactionId)
//
//        val transactionInitiateUser = JSONObject()
//        transactionInitiateUser.put("name", name)
//        transactionInitiateUser.put("email", email)
//        transactionInitiateUser.put("phoneNumber", "+91${phoneNumber}")
//
//        transactionInitiateBody.put("user", transactionInitiateUser)

        val jsonTransactionBody = Gson().toJson(requestBody)
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        md.update(("$jsonTransactionBody/api/transaction$privateApiKey").toByteArray())
        val digest: ByteArray = md.digest()

        val xSignatureHeader = Base64.encodeToString(digest, Base64.NO_WRAP)

        val headers =
            HashMap<String, String>().also {
                it["X-Signature"] = xSignatureHeader
                it["X-Partner-Id"] = privatePartnerId
            }

        Log.d("HEADER IS", headers.toString())
        Log.d("BODY IS", jsonTransactionBody.toString())

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("HERE", "HERE")
            try {
                val response =
                    RetrofitInstance.bharatXApiInstance.initiateTransaction(
                        requestBody,
                        headers
                    )
                Log.d("RESPONSE IS", response.toString())

                runOnUiThread {
                    binding.transactionWebView.loadUrl(response.redirectUrl)
                    binding.transactionSubmit.visibility = View.GONE
                    binding.transactionWebView.visibility = View.VISIBLE
                }


            } catch (e: HttpException) {
                Log.d("RESPONSE IS", e.response().toString())
                Log.d("RESPONSE ERROR IS", e.response().toString())
                Log.d("RESPONSE MESSAGE IS", e.response()?.message().toString())
                Log.d("RESPONSE BODY IS", e.response()?.body().toString())
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.transactionWebView.visibility = View.GONE
        binding.transactionSubmit.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        binding.transactionWebView.visibility = View.GONE
        binding.transactionSubmit.visibility = View.VISIBLE
    }
}