package com.example.mapbox_test_task.gps

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.security.Provider

class GPSTracker(private val context: Context) : Service(), LocationListener {

    // флаг для проверки включенного GPS
    private var isGpsEnable: Boolean = false

    // флаг для проверки включенного Internet
    private var isNetworkEnable: Boolean = false

    // флаг для проверки возможности получить геопозицию
    private var canGetLocation: Boolean = false

    private var location: Location? = null
    var latitude: Double? = null
    var longitude: Double? = null

    private var locationManager: LocationManager? = null

    companion object {
        private const val MIN_DISTANCE: Float = 10f
        private const val MIN_TIME: Long = 10
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager != null) {
                isGpsEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnable =
                    locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!isGpsEnable && !isNetworkEnable) {
                    Toast.makeText(context, "Ничего не включил!", Toast.LENGTH_LONG).show()
                } else {
                    canGetLocation = true
                    if (isNetworkEnable) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME,
                            MIN_DISTANCE,
                            this
                        )
                        Log.d("GPS", "Location from Network")
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null){
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        } else{
                            Log.d("GPS", "location is null from Network")
                        }
                    }
                    else if (isGpsEnable){
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME,
                            MIN_DISTANCE,
                            this
                        )
                        Log.d("GPS", "Location from GPS")
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location != null){
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        } else{
                            Log.d("GPS", "location is null from GPS")
                        }
                    }
                }
            } else
                Toast.makeText(context, "Location manager is null", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
        return location
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }
}