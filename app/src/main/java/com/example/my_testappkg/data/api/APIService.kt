package com.example.my_testappkg.data.api

import com.example.my_testappkg.constant.Constant.app_id
import com.example.my_testappkg.data.model.WeatherDetailResponse
import com.example.my_testappkg.data.model.WeatherHourlyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("weather?")
    suspend fun getWeatherDetail(
        @Query("q") city: String,
        @Query("appid") apiKey: String = app_id
    ): Response<WeatherDetailResponse>


    @GET("onecall?")
    suspend fun getWeatherHourly(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = app_id
    ): Response<WeatherHourlyResponse>

}