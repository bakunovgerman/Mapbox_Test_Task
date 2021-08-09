package com.example.mapbox_test_task.model

import android.content.Context
import com.example.mapbox_test_task.R
import com.example.mapbox_test_task.retrofit.Common
import retrofit2.Response
import android.net.ConnectivityManager
import com.example.mapbox_test_task.model.data.MarkersMap

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

    fun checkInternet(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork!!.isConnectedOrConnecting
    }
}