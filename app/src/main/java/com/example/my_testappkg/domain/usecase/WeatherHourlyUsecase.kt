package com.example.my_testappkg.domain.usecase

import com.example.my_testappkg.data.model.WeatherHourly
import com.example.my_testappkg.data.repository.WeatherHourlyRepository
import com.example.my_testappkg.presentation.base.ResultResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WeatherHourlyUsecase @Inject constructor(private val weatherHourlyRepository: WeatherHourlyRepository) {
    suspend fun execute(lat:Double,lon:Double): Flow<ResultResponse<WeatherHourly>> {
        return weatherHourlyRepository.getSideWeatherHourlyConfig(lat = lat,lon = lon)
    }
}