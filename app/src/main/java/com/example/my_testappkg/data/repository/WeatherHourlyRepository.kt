package com.example.my_testappkg.data.repository

import com.example.my_testappkg.data.api.APIService
import com.example.my_testappkg.data.model.CustomHourly
import com.example.my_testappkg.data.model.Weather
import com.example.my_testappkg.data.model.WeatherHourly
import com.example.my_testappkg.presentation.base.ResultResponse
import com.example.my_testappkg.presentation.base.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.util.ArrayList
import javax.inject.Inject

interface WeatherHourlyRepository {
    suspend fun getSideWeatherHourlyConfig(lat:Double,lon:Double): Flow<ResultResponse<WeatherHourly>>
}

class WeatherHourlyRepositoryImpl @Inject constructor(private val apiClient: APIService) :
    WeatherHourlyRepository {
    override suspend fun getSideWeatherHourlyConfig(lat:Double,lon:Double) = flow {
        emit(
            withTimeout(20000) {
                run {
                    apiClient.getWeatherHourly(lat = lat,lon = lon).run {
                        when {
                            isSuccessful -> {
                                val weather: WeatherHourly = WeatherHourly().apply {
                                    val weather_hourly: ArrayList<CustomHourly> = arrayListOf()
                                    if (body()!!.hourly!!.isNotEmpty()) {
                                        body()!!.current?.let { res ->
                                            body()!!.hourly?.forEach {  hourly ->
                                                temp = res.temp
                                                feels_like = res.feels_like
                                                humidity = res.humidity
                                                dt = res.dt
                                                country = body()!!.timezone
                                                weather_hourly.add(CustomHourly(dt_hourly = hourly.dt,feels_like_hourly =hourly.feels_like,humidity_hourly = hourly.humidity,temp_hourly = hourly.temp ))
                                            }

                                        }
                                        _hourly = weather_hourly
                                    }
                                }
                                Success(data = weather)

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

private fun groupItemModelDefault() = WeatherHourly().apply {
    temp = 0.0
    feels_like = 0.0
    humidity = 0
    dt = 0
}

