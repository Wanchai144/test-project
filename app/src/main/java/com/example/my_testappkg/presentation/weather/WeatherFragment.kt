package com.example.my_testappkg.presentation.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_testappkg.MainActivity
import com.example.my_testappkg.R
import com.example.my_testappkg.data.local.Preferences
import com.example.my_testappkg.data.model.CustomHourly
import com.example.my_testappkg.data.model.WeatherHourly
import com.example.my_testappkg.databinding.WeatherFragmentBinding
import com.example.my_testappkg.extention.viewBinding
import com.example.my_testappkg.presentation.adapter.WeatherHourlyAdapter
import com.example.my_testappkg.presentation.map.MapFragment
import com.example.my_testappkg.presentation.utils.LocationTrack
import com.example.my_testappkg.presentation.utils.MapUtils
import com.example.my_testappkg.presentation.utils.RequestPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.weather_fragment) {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private val binding by viewBinding(WeatherFragmentBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    var dataWeatherHourlyArray: ArrayList<CustomHourly> = arrayListOf()
    var weatherHourlyAdapter: WeatherHourlyAdapter? = null
    lateinit var layoutManager: LinearLayoutManager

    var locationTrack: LocationTrack? = null

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var perferences: Preferences


    var requestPermissions =  RequestPermissions()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationTrack = LocationTrack(requireContext())
        getCurrentLocation()
        setWeatherListAdapter()
        setupDataWeatherScoreView()
    }


    private fun setupViews() = with(binding) {
        if (perferences.getLat() != null  && perferences.getLng() != null){
            Log.d("SAdsadsadsd",perferences.getLocationDefault().toString())
            viewModel.getWeatherDetail(perferences.getLat()!!.toDouble(), perferences.getLng()!!.toDouble())
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setupDataWeatherScoreView() = with(binding) {
        viewModel.onLoadDataWeatherHourlyInfo().observe(viewLifecycleOwner, {
            dataWeatherHourlyArray.addAll(it._hourly!!)
            weatherHourlyAdapter!!.setListAdapterWeather(dataWeatherHourlyArray)
        })
    }


    private fun setWeatherListAdapter() = with(binding){
        weatherHourlyAdapter = WeatherHourlyAdapter(requireActivity(), arrayListOf())
        layoutManager = LinearLayoutManager(
            rvWeather.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        // set rv
        rvWeather.layoutManager = layoutManager
        rvWeather.adapter = weatherHourlyAdapter
        weatherHourlyAdapter!!.notifyDataSetChanged()
    }


    private fun getCurrentLocation() = with(binding) {
        clickStartMap.setOnClickListener {
            val fragment = MapFragment.newInstance()
            val ft: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_container, fragment)
            ft.addToBackStack("fromWeatherFragment")
            ft.commit()
        }

        tvShowAddress.text = ""

        if (requestPermissions.checkPermissions(requireActivity())) {
            if (locationTrack!!.canGetLocation()) {
                if (locationTrack!!.location != null) {
                    val latitude: Double = locationTrack!!.location!!.latitude
                    val longitude: Double = locationTrack!!.location!!.longitude

                    Log.d("sfasfsdf", "lat: $latitude lon: $longitude")

                    val locationName =
                        MapUtils(requireActivity()).getLocationAddress(requireContext(), latitude, longitude)

                    Log.d("sfasfsdf", locationName)


                    if (perferences.getLat() == null && perferences.getLng() == null) {
                        tvShowAddress.text = locationName
                        perferences.saveLocationDefault(locationName)
                        perferences.saveLat(latitude.toString())
                        perferences.saveLng(longitude.toString())
                    }else{
                        tvShowAddress.text = perferences.getLocationDefault()
                        perferences.saveLat(perferences.getLat()!!)
                        perferences.saveLng(perferences.getLng()!!)
                    }
                    setupViews()
                }

            } else {
                locationTrack!!.showSettingsAlert()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        getCurrentLocation()
    }

    override fun onResume() {
        super.onResume()
        val nav = (activity as MainActivity).getNav()
        nav.visibility = View.VISIBLE
        locationTrack = LocationTrack(requireContext())
    }

}