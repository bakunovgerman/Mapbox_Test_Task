package com.example.mapbox_test_task.retrofit

import com.example.mapbox_test_task.model.data.MarkersMap
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServices {
    @GET("/v4/mapbox.mapbox-streets-v8/tilequery/{lon},{lat}.json")
    suspend fun getMarkersMap(@Path("lon") lon: Float,
                      @Path("lat") lat: Float,
                      @Query("radius") radius: Int,
                      @Query("limit") limit: Int,
                      @Query("access_token") access_token: String): Response<MarkersMap>
}