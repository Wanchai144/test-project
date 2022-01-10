package com.example.my_testappkg.data.model

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("temp")
    var temp: Double?= 0.0
    @SerializedName("feels_like")
    var feels_like: Double? =0.0
    @SerializedName("temp_min")
    var temp_min: Double? = 0.0
    @SerializedName("temp_max")
    var temp_max: Double? = 0.0
    @SerializedName("humidity")
    var humidity: Int? = 0
    @SerializedName("country")
    var country: String? =""
    @SerializedName("date_time")
    var date_time: Int?= 0
}


