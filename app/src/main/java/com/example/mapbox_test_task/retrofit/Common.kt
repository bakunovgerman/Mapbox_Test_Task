package com.example.mapbox_test_task.retrofit

object Common {
    private const val BASE_URL = "https://api.mapbox.com"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}