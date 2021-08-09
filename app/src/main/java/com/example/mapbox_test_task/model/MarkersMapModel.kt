package com.example.mapbox_test_task.model

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.mapbox_test_task.App
import com.example.mapbox_test_task.R
import com.example.mapbox_test_task.retrofit.Common
import com.google.android.gms.location.LocationListener
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MarkersMapModel(private val context: Context) {
    suspend fun getMarkersMapAPI(lon: Float, lat: Float): Response<MarkersMap> {
        val mService = Common.retrofitService
        return mService.getMarkersMap(
            lon,
            lat,
            1000,
            50,
            context.getString(R.string.mapbox_access_token)
        )
    }
}