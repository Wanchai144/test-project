package com.example.my_testappkg.data.model

import com.google.gson.annotations.SerializedName

data class WeatherDetailResponse(
    @SerializedName("base")
    val base: String?= "",
    @SerializedName("clouds")
    val clouds: Clouds?= null,
    @SerializedName("cod")
    val cod: Int?= 0,
    @SerializedName("dt")
    val dt: Int?= 0,
    @SerializedName("id")
    val id: Int?= 0,
    @SerializedName("main")
    val main: Main? = null,
    @SerializedName("name")
    val name: String?= "",
    @SerializedName("sys")
    val sys: Sys? = null,
    @SerializedName("timezone")
    val timezone: Int?= 0,
    @SerializedName("visibility")
    val visibility: Int?= 0,
    @SerializedName("weather")
    val weather: List<Weathers>? = arrayListOf(),
)

data class Clouds(
    @SerializedName("all")
    val all: Int?= 0
)

data class Main(
    @SerializedName("feels_like")
    val feels_like: Double? =0.0,
    @SerializedName("grnd_level")
    val grnd_level: Int? = 0,
    @SerializedName("humidity")
    val humidity: Int? = 0,
    @SerializedName("pressure")
    val pressure: Int? =0,
    @SerializedName("sea_level")
    val sea_level: Int? =0,
    @SerializedName("temp")
    val temp: Double?= 0.0,
    @SerializedName("temp_max")
    val temp_max: Double? = 0.0,
    @SerializedName("temp_min")
    val temp_min: Double? = 0.0
)

data class Sys(
    @SerializedName("country")
    val country: String?= "",
)

data class Weathers(
    @SerializedName("description")
    val description: String? ="",
    @SerializedName("icon")
    val icon: String? ="",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("main")
    val main: String? =""
)

