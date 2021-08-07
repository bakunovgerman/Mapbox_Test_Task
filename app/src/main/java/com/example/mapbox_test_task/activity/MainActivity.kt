package com.example.mapbox_test_task.activity

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import android.location.LocationManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mapbox_test_task.R
import com.example.mapbox_test_task.gps.GPSTracker
import com.example.mapbox_test_task.model.MarkersMap
import com.example.mapbox_test_task.retrofit.Common
import com.example.mapbox_test_task.viewModel.MainActivityViewModel
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback,
    LocationListener {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var locationManager: LocationManager
    private var longitude: Float? = null
    private var latitude: Float? = null
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.markersMap.observe(this, Observer(::addMarkers))
    }

    private fun addMarkers(markersMap: MarkersMap) {
        Toast.makeText(this, "addMarkers", Toast.LENGTH_LONG).show()
//        if (markersMap != null){
//            markersMap.features.forEach {
//                mapboxMap.addMarker(
//                    MarkerOptions().position(
//                        LatLng(
//                            it.geometry.coordinates[1],
//                            it.geometry.coordinates[0]
//                        )
//                    )
//                )
//            }
//        } else{
//            Toast.makeText(this, "Маркеры не получены!", Toast.LENGTH_LONG).show()
//        }

    }

//    @SuppressLint("MissingPermission")
//    private fun getLocationDevice() {
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            //locationManager.requestLocationUpdates(, 5000, 5f, this);
//            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            if (location != null) {
//                longitude = location.longitude.toFloat()
//                latitude = location.latitude.toFloat()
//                //Log.d("location", "location: $longitude; $latitude")
//                getMarkersMapAPI(longitude!!, latitude!!)
//            } else {
//                Toast.makeText(this, "Данные геопозиции не получены!", Toast.LENGTH_LONG).show()
//            }
//
//        } else {
//            permissionsManager = PermissionsManager(this)
//            permissionsManager.requestLocationPermissions(this)
//        }
//    }

    override fun onLocationChanged(location: Location) {
        Log.d("location", location.latitude.toString())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("TAG", "onRequestPermissionsResult")
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Log.d("TAG", "onExplanationNeeded")
        Toast.makeText(this, "user_location_permission_explanation", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        Log.d("TAG", "onPermissionResult")
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, "user_location_permission_not_granted", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        Log.d("TAG", "onMapReady")
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.Builder().fromUri("mapbox://styles/mapbox/streets-v11")
        ) {
            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            enableLocationComponent(it)
        }
//        mapboxMap.addOnMapClickListener {
//            mapboxMap.addMarker(MarkerOptions().position(it))
//            true
//        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        Log.d("TAG", "enableLocationComponent")
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.mapboxGreen))
                .build()
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            // Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {

                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

                // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
            mainActivityViewModel.loadMarkersMap()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}