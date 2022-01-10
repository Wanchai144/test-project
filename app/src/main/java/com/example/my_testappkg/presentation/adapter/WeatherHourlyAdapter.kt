package com.example.my_testappkg.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.my_testappkg.R
import com.example.my_testappkg.data.model.CustomHourly
import com.example.my_testappkg.data.model.WeatherHourly
import com.example.my_testappkg.databinding.LoadMoreItemBinding
import com.example.my_testappkg.databinding.WeatherItemBinding
import com.example.my_testappkg.presentation.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherHourlyAdapter(
    private var context: Context,
    private var weather: ArrayList<CustomHourly>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_LOAD = 0
        private const val TYPE_LOAD_MORE = 1
    }

    fun setListAdapterWeather(lists: ArrayList<CustomHourly>) {
        weather.clear()
        this.weather.addAll(lists)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return weather.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ViewDataBinding
        return when (viewType) {
            TYPE_LOAD -> {
                binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.weather_item,
                    parent,
                    false
                )
                ViewHolder(binding as WeatherItemBinding)
            }
            TYPE_LOAD_MORE -> {
                binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.load_more_item,
                    parent,
                    false
                )
                ViewHolderLoadMore(binding as LoadMoreItemBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val bindingView = holder.binding
            val temp = weather[position].temp_hourly!! - 273.15
             bindingView.tvWeather.text = "${Utils().formatdouble(temp)} °C"
             val updatedAtText =  SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(weather[position].dt_hourly!!.toLong()*1000))
             bindingView.tvTime.text = updatedAtText
             bindingView.tvHumidity.text ="H: ${weather[position].humidity_hourly} %"
        }
    }


    class ViewHolderLoadMore(internal val binding: LoadMoreItemBinding): RecyclerView.ViewHolder(binding.root)

    class ViewHolder(internal var binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root)

}