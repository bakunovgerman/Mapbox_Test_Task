package com.example.mapbox_test_task.model

import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.mapbox_test_task.App
import com.example.mapbox_test_task.R
import com.example.mapbox_test_task.gps.GPSTracker
import com.example.mapbox_test_task.retrofit.Common
import retrofit2.Call
import retrofit2.Response

class MarkersMapModel {

    fun getLocationGPSTracker(): Location? {
        val gpsTracker = GPSTracker(App.applicationContext)
        return gpsTracker.getLocation()
    }

    suspend fun getMarkersMapAPI(lon: Float, lat: Float): Response<MarkersMap> {
        val mService = Common.retrofitService
        //Log.d("markersMap", mService.getMarkersMap(lon, lat, 1000, 50, getString(R.string.mapbox_access_token)).request().toString())
        return mService.getMarkersMap(lon, lat, 1000, 50, App.applicationContext.getString(R.string.mapbox_access_token))
//            .enqueue(object : retrofit2.Callback<MarkersMap> {
//                override fun onResponse(call: Call<MarkersMap>, response: Response<MarkersMap>) {
//                    if (response.isSuccessful) {
//                        val markersMap: MarkersMap? = response.body()
////                        markersMap?.features?.forEach {
////                            mapboxMap.addMarker(
////                                MarkerOptions().position(
////                                    LatLng(
////                                        it.geometry.coordinates[1],
////                                        it.geometry.coordinates[0]
////                                    )
////                                )
////                            )
////                        }
//                        Log.d("markersMap", markersMap?.features?.size.toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<MarkersMap>, t: Throwable) {
//                    Log.d("markersMap-Throwable", t.toString())
//                    Toast.makeText(App.applicationContext, "Retrofit onFailure", Toast.LENGTH_LONG).show()
//                }
//            })
    }
}