//package com.example.my_testappkg.data.api
//
//import android.content.Context
//import com.example.my_testappkg.constant.Constant
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//
//import retrofit2.CallAdapter
//import retrofit2.Converter
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import java.util.concurrent.TimeUnit
//
//class BaseHttpClient : KoinComponent {
//
//    open class ApiScalarsAndGsonBuilder(
//        val gsonConverterFactory: Converter.Factory,
//        val scalarsConverterFactory: Converter.Factory,
//        val rxJavaAdapterFactory: CallAdapter.Factory
//    ) {
//
//        val  okHttpClient = OkHttpClient
//            .Builder()
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .connectTimeout(5, TimeUnit.MINUTES)
//            .writeTimeout(5, TimeUnit.MINUTES)
//            .readTimeout(5, TimeUnit.MINUTES)
//            .build()
//
//        inline fun <reified T> build(): T {
//            return Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(Constant.BASE_URL)
//                .addConverterFactory(gsonConverterFactory)
//                .addConverterFactory(scalarsConverterFactory)
//                .addCallAdapterFactory(rxJavaAdapterFactory)
//                .build()
//                .create(T::class.java)
//        }
//
//    }
//
//}
//
