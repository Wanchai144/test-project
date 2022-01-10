package com.example.my_testappkg.di

import android.content.Context
import com.example.my_testappkg.constant.Constant
import com.example.my_testappkg.data.api.APIService
import com.example.my_testappkg.data.local.Preferences
import com.example.my_testappkg.data.repository.WeatherDetailRepository
import com.example.my_testappkg.data.repository.WeatherDetailRepositoryImpl
import com.example.my_testappkg.data.repository.WeatherHourlyRepository
import com.example.my_testappkg.data.repository.WeatherHourlyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = Constant.BASE_URL


    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideOkHttpClient(logger:HttpLoggingInterceptor): OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(logger)
        val okHttp = okHttpClient.build()
        return okHttp
    }

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String,okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) =
        retrofit.create(APIService::class.java)


    @Singleton
    @Provides
    fun provideWeatherRepository(adiService: APIService) : WeatherDetailRepository {
        return WeatherDetailRepositoryImpl(adiService)
    }

    @Singleton
    @Provides
    fun provideWeatherHourlyRepository(adiService: APIService) : WeatherHourlyRepository {
        return WeatherHourlyRepositoryImpl(adiService)
    }



}



