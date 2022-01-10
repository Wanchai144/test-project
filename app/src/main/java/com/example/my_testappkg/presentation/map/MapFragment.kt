package com.example.my_testappkg.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_testappkg.MainActivity
import com.example.my_testappkg.R
import com.example.my_testappkg.data.local.Preferences
import com.example.my_testappkg.databinding.FragmentMainBinding
import com.example.my_testappkg.databinding.FragmentMapBinding
import com.example.my_testappkg.extention.viewBinding
import com.example.my_testappkg.presentation.utils.LocationTrack
import com.example.my_testappkg.presentation.utils.MapUtils
import com.example.my_testappkg.presentation.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
    val TAG = "MapFragment"

    companion object {
        fun newInstance() = MapFragment()
    }


    lateinit var googleMap: GoogleMap

    var last = String()
    var long = String()

    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val binding by viewBinding(FragmentMapBinding::bind)


    var locationTrack: LocationTrack? = null

    @Inject
    lateinit var mapUtils: MapUtils

    @Inject
    lateinit var perferences: Preferences

    var latByGuest = ""
    var lonByGuest = ""
    var adNameByGuest = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        binding.mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationTrack = LocationTrack(requireContext())
        Places.initialize(
            requireActivity(),
            requireActivity().getString(R.string.google_map_api_key)
        )
        setHideTabBar()
        initView()

    }

    private fun initView() = with(binding){
        confirmLocation.setOnClickListener {
            Log.d("sadsadsad", adNameByGuest)
            perferences.saveLat(latByGuest)
            perferences.saveLng(lonByGuest)
            perferences.saveLocationDefault(adNameByGuest)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        locationTrack!!.cancelGetLocation()
    }

    private fun setHideTabBar() {
        val nav = (activity as MainActivity).getNav()
        nav.visibility = View.GONE
    }


    private fun onAction(map: GoogleMap) {
        binding.btnChangeMyLocation.setOnClickListener {
            moveCameraToCurrentLocation()
        }
    }

    private fun moveCameraToCurrentLocation() {
        if (locationTrack!!.canGetLocation()) {
            if (locationTrack!!.location != null) {
                val latitude: Double = locationTrack!!.location!!.latitude
                val longitude: Double = locationTrack!!.location!!.longitude

                val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude, longitude),
                    18f
                )
                googleMap.animateCamera(cameraUpdate)
            } else {
                val location = MapUtils(requireActivity()).mapGetLocationCurrent(requireActivity())
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val cameraUpdate: CameraUpdate =
                        CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                    googleMap.animateCamera(cameraUpdate)
                }
            }

        } else {
            val location = MapUtils(requireActivity()).mapGetLocationCurrent(requireActivity())
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraUpdate: CameraUpdate =
                    CameraUpdateFactory.newLatLngZoom(latLng, 18f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }


    override fun onMapReady(map: GoogleMap) = with(binding){
        googleMap = map
        try {
                if (perferences.getLat() != null && perferences.getLng() != null) {
                    mapUtils.mapMarkerCameraMove(
                        requireActivity(),
                        map,
                        perferences.getLat()!!.toDouble(),
                        perferences.getLng()!!.toDouble(),
                        binding.markerFix
                    )

                } else {
                    val task = fusedLocationClient.lastLocation

                    task.addOnSuccessListener {
                        if (it != null) {
                            mapUtils.mapMarkerCameraMove(
                                requireActivity(),
                                map,
                                it.latitude,
                                it.longitude,
                                binding.markerFix
                            )
                        } else {
                            mapUtils.mapMarkerCameraMove(
                                requireActivity(),
                                map,
                                0.0,
                                0.0,
                                binding.markerFix
                            )
                        }
                    }
                }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false

        map.setOnCameraChangeListener {
            //  viewModel.latLonAddAddress.value = LatLng(it.target.latitude, it.target.longitude)

//            perferences.saveLng(it.target.longitude.toString())
                    val address = mapUtils.getAddressFromLocation(
                        requireActivity(),
                        it.target.latitude,
                        it.target.longitude
                    )
                    latByGuest = it.target.latitude.toString()
                    lonByGuest = it.target.longitude.toString()
                    adNameByGuest = address


            Log.d("mapMarker", "lat:${it.target.latitude}, lng:${it.target.longitude}")
        }


        onAction(map)

    }

    override fun onMyLocationChange(p0: Location) {
        //
    }

}