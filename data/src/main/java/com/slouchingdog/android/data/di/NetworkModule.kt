package com.slouchingdog.android.data.di

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.slouchingdog.android.data.remote.EnumTypeAdapter
import com.slouchingdog.android.data.remote.HabitAPIService
import com.slouchingdog.android.data.remote.HabitApiClient
import com.slouchingdog.android.data.remote.RetryInterceptor
import com.slouchingdog.android.domain.entity.HabitType
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule() {
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Singleton
    @Provides
    fun provideRetryInterceptor(): RetryInterceptor {
        return RetryInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        retryInterceptor: RetryInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(HabitApiClient.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(HabitType::class.java, EnumTypeAdapter())
                        .setStrictness(Strictness.LENIENT)
                        .create()
                )
            ).build()
    }

    @Singleton
    @Provides
    fun provideHabitService(retrofit: Retrofit): HabitApiClient {
        return retrofit.create(HabitApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideService(apiClient: HabitApiClient): HabitAPIService {
        return HabitAPIService(apiClient)
    }
}