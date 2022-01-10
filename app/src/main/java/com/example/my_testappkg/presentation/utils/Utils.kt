package com.example.my_testappkg.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class Utils {



    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDt(timestamp: Long): String {
        val timestampAsDateString = DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(timestamp))
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val date = LocalDate.parse(timestampAsDateString, formatter)
        val date_time = "data: $date time: $currentTime"
        return date_time
    }



    fun formatdouble(price:Double):String{
        val df = DecimalFormat("#.##")
        val sumprice: String = df.format(price)
        return sumprice
    }

}