package com.example.my_testappkg.data.model

import com.google.gson.annotations.SerializedName

data class WeatherHourlyResponse(
    @SerializedName("current")
    val current: Current?= null,
    @SerializedName("hourly")
    val hourly: List<Hourly>? = arrayListOf(),
    @SerializedName("timezone")
    val timezone: String?= "",
    @SerializedName("timezone_offset")
    val timezone_offset: Int?= 0
)

data class Current(
    @SerializedName("dt")
    val dt: Int? = 0,
    @SerializedName("feels_like")
    val feels_like: Double? = 0.0,
    @SerializedName("humidity")
    val humidity: Int? = 0,
    @SerializedName("temp")
    val temp: Double? = 0.0,
)

data class Hourly(
    @SerializedName("dt")
    val dt: Int? = 0,
    @SerializedName("feels_like")
    val feels_like: Double?= 0.0,
    @SerializedName("humidity")
    val humidity: Int?= 0,
    @SerializedName("temp")
    val temp: Double?= 0.0,
)


