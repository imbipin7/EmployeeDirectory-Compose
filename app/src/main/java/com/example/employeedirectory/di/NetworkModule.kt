package com.example.employeedirectory.di

import android.content.Context
import android.content.pm.PackageManager
import com.example.employeedirectory.api.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL = "https://dummy.restapiexample.com/"
const val NETWORK_TIMEOUT = 60L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun connectionTimeOut() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }
        return OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method, original.body).build()
            chain.proceed(request)
        }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

}

