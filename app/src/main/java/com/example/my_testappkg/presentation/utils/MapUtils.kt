package com.example.my_testappkg.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import com.example.my_testappkg.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class MapUtils @Inject constructor(@ApplicationContext private var context: Context) {
    private var mMapZoomLevel = 15f
    var mLocationManager: LocationManager? = null


    fun mapAddMarker(context: Context, mMap: GoogleMap, mLatitude: Double, mLongitude: Double) {
        val location = LatLng(mLatitude, mLongitude)
        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(context)))
                .title("Marker")
        )
    }


    fun mapMarkerCameraMove(
        context: Context,
        mMap: GoogleMap,
        mLatitude: Double,
        mLongitude: Double,
        view: View
    ) {
        val location = LatLng(mLatitude, mLongitude)
        mMap.clear()



        val marker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker")
                .draggable(true)
                .visible(false)

        )


        val center = CameraUpdateFactory.newLatLng(location)
        val zoom = CameraUpdateFactory.newLatLngZoom(location, 18f)

        mMap.moveCamera(center)
        mMap.animateCamera(zoom)

        val animSlide = AnimationUtils.loadAnimation(context, R.anim.marker_aim)


        mMap.setOnCameraMoveListener {
            val midLatLng = mMap.cameraPosition.target
            if (marker != null) {
                marker.position = midLatLng
                view.startAnimation(animSlide)
            }

        }

    }

    @SuppressLint("MissingPermission")
    fun mapGetLocationCurrent(context: Context): Location? {
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }

        Log.d("sdfnbasydfa", "lat: ${bestLocation?.latitude} lon: ${bestLocation?.longitude}")
        return bestLocation
    }

    private fun getMarkerBitmapFromView(context: Context): Bitmap {
        val customMarkerView: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.custom_marker_layout,
                null
            )

//        val markerImage: ImageView = customMarkerView.findViewById(R.id.marker_image) as ImageView

//        Picasso.get()
//            .load(R.drawable.maplocation)
//            .transform(CircleTransform())
//            .into(markerImage, object : Callback {
//                override fun onSuccess() {
//                    val source: Bitmap = (markerImage.drawable as BitmapDrawable).bitmap
//                    val drawable: RoundedBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(context.resources, source)
//                    drawable.isCircular = true
//                    drawable.cornerRadius = (source.width.coerceAtLeast(source.height)
//                            / 30.0f)
//                    markerImage.setImageDrawable(drawable)
//                }
//
//                override fun onError(e: Exception?) {
//                    e!!.printStackTrace()
//                }
//            })

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    fun getLocationAddress(context: Context, mCurrentLat: Double, mCurrentLng: Double): String {


        var mLocation = ""
        val geocoder = Geocoder(context, Locale("th"))
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(mCurrentLat, mCurrentLng, 1)
            if (addresses.isNotEmpty()) {
                if (addresses[0].getAddressLine(0) != null) {
                    mLocation = addresses[0].getAddressLine(0)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            mLocation = "ไม่พบตำแหน่ง"
        }
        return mLocation
    }



    fun getAddressFromLocation(context: Context, mCurrentLat: Double, mCurrentLng: Double): String {
        var mLocation = ""
        try {
            val geo = Geocoder(context, Locale("th"))
            val addresses = geo.getFromLocation(mCurrentLat, mCurrentLng, 1)
            if (addresses.isEmpty()) {
                mLocation = "กำลังรอตำแหน่ง"
            } else {
                if (addresses.size > 0) {
                    mLocation = addresses[0].getAddressLine(0)
                    //  yourtextfieldname.setText(addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].adminArea + ", " + addresses[0].countryName)
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace() // getFromLocation() may sometimes fail
            mLocation = "ไม่พบตำแหน่ง"
            Log.d("mapUtils", e.message.toString())
        }
        return mLocation
    }


}