package com.slouchingdog.android.slouchyhabit.data.repository

import android.util.Log
import com.slouchingdog.android.slouchyhabit.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val retryDelayMillis: Long = 3000L) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Authorization", BuildConfig.AUTHORIZATION_KEY)
            .build()

        var response: Response? = null
        while (response == null || !response.isSuccessful) {
            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
                response.close()
            } catch (e: Exception) {
                Log.e("API REQUEST ERROR", e.toString())
                response?.close()
            }
            Thread.sleep(retryDelayMillis)
        }
        return response
    }
}