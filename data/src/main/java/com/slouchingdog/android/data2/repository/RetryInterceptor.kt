package com.slouchingdog.android.data2.repository

import android.util.Log
import com.slouchingdog.android.data2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(
    private val retryDelayMillis: Long = 3000L,
    private var retryTimes: Int = 2
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Authorization", BuildConfig.AUTHORIZATION_KEY)
            .build()

        var response: Response? = null
        while (response == null || !response.isSuccessful && retryTimes > 0) {
            response?.close()
            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: Exception) {
                Log.e("API REQUEST ERROR", e.toString())
            }
            Thread.sleep(retryDelayMillis)
            retryTimes--
        }
        return response
    }
}