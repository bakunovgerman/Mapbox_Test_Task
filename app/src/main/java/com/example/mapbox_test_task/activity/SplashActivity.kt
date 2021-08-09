package com.example.mapbox_test_task.activity

import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mapbox_test_task.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

class SplashActivity : AppCompatActivity(), PermissionsListener {

    private lateinit var locationRequest: LocationRequest
    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationRequest = LocationRequest.create()
            // запрос на включение gps
            enableGps()
        }
        else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun enableGps() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(applicationContext)
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener(OnCompleteListener {
            try {
                val response = it.getResult(ApiException::class.java)
                Log.d("onActivityResult", "response yes")
                startActivity(Intent(this, MainActivity::class.java))
            } catch (e: ApiException) {
                if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                            this@SplashActivity,
                            MainActivity.REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {

                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("CheckLifeCycle", "onActivityResult")
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            locationRequest = LocationRequest.create()
            // запрос на включение gps
            enableGps()
        } else {
            Toast.makeText(this, "user_location_permission_not_granted", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}