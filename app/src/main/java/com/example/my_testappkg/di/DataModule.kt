package com.example.my_testappkg.di
//
//import com.example.my_testappkg.data.repository.WeatherDetailRepository
//import com.example.my_testappkg.data.repository.WeatherDetailRepositoryImpl
//import com.example.my_testappkg.domain.usecase.WeatherDetailUsecase
//import com.example.my_testappkg.domain.usecase.WeatherDetailUsecaseImpl
////import com.example.my_testappkg.domain.usecase.WeatherDetailUsecaseImpl
//import org.koin.dsl.module
//
//val interactionModule = module {
//    factory<WeatherDetailRepository> {
//        WeatherDetailRepositoryImpl(apiClient = get())
//    }
//
//    factory<WeatherDetailUsecase> {
//        WeatherDetailUsecaseImpl(
//            weatherRepository = get()
//        )
//    }
//}