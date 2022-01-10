package com.example.my_testappkg.data.repository

import com.example.my_testappkg.data.api.APIService
import com.example.my_testappkg.data.model.Weather
import com.example.my_testappkg.presentation.base.ResultResponse
import com.example.my_testappkg.presentation.base.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


interface WeatherDetailRepository {
    suspend fun getSideWeatherConfig(city:String): Flow<ResultResponse<Weather>>
}

class WeatherDetailRepositoryImpl @Inject constructor(private val apiClient: APIService) :
    WeatherDetailRepository {
    override suspend fun getSideWeatherConfig(city:String) = flow {
        emit(
            withTimeout(20000) {
                run {
                    apiClient.getWeatherDetail(city).run {
                        when {
                            isSuccessful -> {
                                if (body()!!.weather!!.isNotEmpty()) {
                                    val weather: Weather = Weather().apply {
                                        body()!!.main?.let { res ->
                                            body()!!.sys?.let { sys ->
                                                temp = res.temp
                                                feels_like = res.feels_like
                                                temp_min = res.temp_min
                                                temp_max = res.temp_max
                                                humidity = res.humidity
                                                country = body()!!.name
                                                date_time = body()!!.dt
                                            }
                                        }
                                    }
                                    Success(data = weather)
                                } else {
                                    Success(data = groupItemModelDefault())
                                }
                            }
                            else -> {
                                Success(data = groupItemModelDefault())
                            }
                        }
                    }
                }
            }
        )
    }
}

private fun groupItemModelDefault() = Weather().apply {
    temp = 0.0
    feels_like = 0.0
    temp_min =0.0
    temp_max = 0.0
    humidity = 0
    country = ""
    date_time = 0
}

