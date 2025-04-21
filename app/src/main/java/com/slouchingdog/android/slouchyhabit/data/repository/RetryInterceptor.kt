package com.slouchingdog.android.slouchyhabit.data.repository

import android.util.Log
import com.slouchingdog.android.slouchyhabit.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val retryDelayMillis: Long = 1000L) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder =
            originalRequest.newBuilder().header("Authorization", BuildConfig.AUTHORIZATION_KEY)
        val newRequest = builder.build()
        var response: Response? = null
        while (response == null || !response.isSuccessful) {
            try {
                response = chain.proceed(newRequest)
            } catch (e: Exception) {
                Log.e("API REQUEST ERROR", e.toString())
                response?.close()
            }
            Thread.sleep(retryDelayMillis)
        }
        return response
    }
}