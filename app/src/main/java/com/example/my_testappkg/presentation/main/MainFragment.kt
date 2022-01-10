package com.example.my_testappkg.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.my_testappkg.MainActivity
import com.example.my_testappkg.R
import com.example.my_testappkg.databinding.FragmentMainBinding
import com.example.my_testappkg.extention.gone
import com.example.my_testappkg.extention.viewBinding
import com.example.my_testappkg.extention.visible
import com.example.my_testappkg.presentation.utils.LocationTrack
import com.example.my_testappkg.presentation.utils.RequestPermissions
import com.example.my_testappkg.presentation.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()


    lateinit var utils: Utils

    var requestPermissions =  RequestPermissions()

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        setupViews()
        setupDataWeatherScoreView()
    }

    private fun setupViews() = with(binding) {
        btSubmit.setOnClickListener {
            val city = edtCity.text.toString()
            if (city != "") {
                tvError.gone()
                viewModel.getWeatherDetail(city)
            } else {
                tvError.visible()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setupDataWeatherScoreView() = with(binding) {
        viewModel.onLoadDataWeatherInfo().observe(viewLifecycleOwner, {
            val temp = it.temp!! - 273.15
            val temp_min = it.temp_min!! - 273.15
            val temp_max = it.temp_max!! - 273.15
            tvCity.text = "Country ${it.country}"
            tvHumidity.text = "Humidity ${it.humidity} %"
            tvTemp.text = "Temp ${utils.formatdouble(temp)} °C"
            tvTempMin.text = "Temp_min ${utils.formatdouble(temp_min)} °C"
            tvTempMax.text = "Temp_max ${utils.formatdouble(temp_max)} °C"
            tvError.gone()
            tvDateTime.text = utils.formatDt(it.date_time!!.toLong())

            btC.setOnClickListener { res ->
                tvDateTime.text = utils.formatDt(it.date_time!!.toLong())
                tvCity.text = "Country ${it.country}"
                tvHumidity.text = "Humidity ${it.humidity} %"
                tvTemp.text = "Temp ${utils.formatdouble(temp)} °C"
                tvTempMin.text = "Temp_min ${utils.formatdouble(temp_min)} °C"
                tvTempMax.text = "Temp_max ${utils.formatdouble(temp_max)} °C"
            }

            btF.setOnClickListener {  its ->
                val temp_f = 1.8 * temp + 32
                val temp_min_f = 1.8 * temp_min + 32
                val temp_max_f = 1.8 * temp_max + 32
                tvDateTime.text = utils.formatDt(it.date_time!!.toLong())
                tvCity.text = "Country ${it.country}"
                tvHumidity.text = "Humidity ${it.humidity} %"
                tvTemp.text = "Temp ${utils.formatdouble(temp_f)} °F"
                tvTempMin.text = "Temp_min ${utils.formatdouble(temp_min_f)} °F"
                tvTempMax.text = "Temp_max ${utils.formatdouble(temp_max_f)} °F"
            }
        })
        viewModel.onErrorDataWeatherInfo().observe(viewLifecycleOwner, {
            //onError
            tvError.visible()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission", "CheckResult")
    private fun getLastLocation() {
        if (requestPermissions.checkPermissions(requireActivity())) {
        } else {
            requestPermissions.checkPermissionLocation(requireActivity()) {}
        }
    }

    override fun onResume() {
        super.onResume()
        val nav = (activity as MainActivity).getNav()
        nav.visibility = View.VISIBLE
    }
}