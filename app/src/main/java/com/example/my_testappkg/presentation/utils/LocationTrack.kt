package com.example.my_testappkg.presentation.utils

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import javax.inject.Inject
import javax.inject.Singleton



class LocationTrack (private val mContext: Context) : Service(), LocationListener {
    var checkGPS = false
    var checkNetwork = false
    var canGetLocation = false
    var loc: Location? = null
    var latitude = 0.0
    var longitude = 0.0
    var locationManager: LocationManager? = null// TODO: Consider calling

    val location: Location?
        get() {
            try {
                locationManager = mContext
                    .getSystemService(LOCATION_SERVICE) as LocationManager

                // get GPS status
                checkGPS = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)

                // get network provider status
                checkNetwork = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)


                if (!checkGPS && !checkNetwork) {
                    Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    canGetLocation = true
                    if (checkGPS) {
                        if (ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION

                            ) != PackageManager.PERMISSION_GRANTED
                        )
                            locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                0.toLong(),
                                0.toFloat(),
                                this
                            )
                        if (locationManager != null) {
                            loc =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (loc != null) {
                                latitude = loc!!.latitude
                                longitude = loc!!.longitude

                            }
                        }
                    }

                    if (checkNetwork) {
                        if (ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                mContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        )
                            locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                0.toLong(),
                                0.toFloat(),
                                this
                            )
                        if (locationManager != null) {
                            loc =
                                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (loc != null) {
                                latitude = loc!!.latitude
                                longitude = loc!!.longitude

                            }
                        }
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return loc
        }

    @JvmName("getLongitude1")
    fun getLongitude(): Double {
        if (loc != null) {
            longitude = loc!!.longitude
        }
        return longitude
    }

    @JvmName("getLatitude1")
    fun getLatitude(): Double {
        if (loc != null) {
            latitude = loc!!.latitude
        }
        return latitude
    }

    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setTitle("ยังไม่ได้เปิดใช้งาน GPS!!")
        alertDialog.setMessage("คุณต้องการเปิดใช้งาน GPS หรือไม่?")
        alertDialog.setPositiveButton("ตกลง") { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
        alertDialog.setNegativeButton("ยกเลิก") { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    fun cancelGetLocation() {
        locationManager!!.removeUpdates(this)
        loc = null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
    override fun onProviderEnabled(s: String) {}
    override fun onProviderDisabled(s: String) {}

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = 1000.toLong()
    }

    init {
        location
    }
}