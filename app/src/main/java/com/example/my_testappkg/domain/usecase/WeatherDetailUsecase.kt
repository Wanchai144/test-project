package com.example.my_testappkg.domain.usecase

import com.example.my_testappkg.data.model.Weather
import com.example.my_testappkg.data.repository.WeatherDetailRepository
import com.example.my_testappkg.presentation.base.ResultResponse
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


//
//interface WeatherDetailUsecase {
//    suspend fun execute(city:String): Flow<ResultResponse<Weather>>
//}
//
//class WeatherDetailUsecaseImpl @Inject constructor(
//    private val weatherRepository: WeatherDetailRepository
//) : WeatherDetailUsecase {
//    override suspend fun execute(city:String): Flow<ResultResponse<Weather>> {
//        return  weatherRepository.getSideWeatherConfig(city = city)
//    }
//}


class WeatherDetailUsecase @Inject constructor(private val weatherRepository: WeatherDetailRepository) {
    suspend fun execute(city:String): Flow<ResultResponse<Weather>> {
        return weatherRepository.getSideWeatherConfig(city = city)
    }
}