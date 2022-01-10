package com.example.my_testappkg.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.my_testappkg.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class RequestPermissions  {
    fun checkPermissions(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun checkPermissionsCallPhone(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private val MY_PERMISSIONS_REQUEST = 32
    fun requestPermission(activity: Activity, permission: String) {
        if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(permission),
                MY_PERMISSIONS_REQUEST
            )
        }
    }


    fun checkPermissionLocation(
        fragmentActivity: FragmentActivity,
        ClickCallback: ((Boolean) -> Unit)
    ) {
        Dexter.withActivity(fragmentActivity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        ClickCallback.invoke(true)
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        DialogPresenter(fragmentActivity).dialogInfoChangeButton(
                            fragmentActivity.getString(R.string.permission_req),
                            "",
                            fragmentActivity.getString(R.string.setting2),
                            fragmentActivity.getString(R.string.not_now)
                        ) { i ->
                            if (i) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri: Uri =
                                    Uri.fromParts("package", fragmentActivity.packageName, null)
                                intent.data = uri
                                fragmentActivity.startActivity(intent)
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }


}