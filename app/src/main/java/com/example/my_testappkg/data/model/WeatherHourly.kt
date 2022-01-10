package com.example.my_testappkg.data.model

import com.google.gson.annotations.SerializedName

data  class WeatherHourly (
    @SerializedName("dt")
    var dt: Int? = 0,
    @SerializedName("feels_like")
    var feels_like: Double? = 0.0,
    @SerializedName("humidity")
    var humidity: Int? = 0,
    @SerializedName("temp")
    var temp: Double? = 0.0,
    @SerializedName("country")
    var country: String?= "",
    @SerializedName("hourly")
    var _hourly: List<CustomHourly>? = arrayListOf()
)

data class CustomHourly (
    @SerializedName("dt_hourly")
    var dt_hourly: Int? = 0,
    @SerializedName("feels_like_hourly")
    var feels_like_hourly: Double? = 0.0,
    @SerializedName("humidity_hourly")
    var humidity_hourly: Int? = 0,
    @SerializedName("temp_hourly")
    var temp_hourly: Double? = 0.0
)


