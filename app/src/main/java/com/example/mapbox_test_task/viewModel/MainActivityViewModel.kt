package com.example.mapbox_test_task.viewModel

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox_test_task.model.MarkersMap
import com.example.mapbox_test_task.model.MarkersMapModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    companion object{
        private const val TAG = "viewModelLog"
    }

    // init LiveData
    val markersMap: LiveData<MarkersMap> get() = _markersMap
    private val _markersMap = MutableLiveData<MarkersMap>()

    // init Model
    private lateinit var markersMapModel: MarkersMapModel

    fun loadMarkersMap() {
        val viewModelScopeCustom = viewModelScope + Dispatchers.Main
        viewModelScopeCustom.launch {
            markersMapModel = MarkersMapModel()
            val location: Location? = markersMapModel.getLocationGPSTracker()
//            if (location != null) {
//                var response: Response<MarkersMap>
//                withContext(Dispatchers.IO) {
//                    response = markersMapModel.getMarkersMapAPI(
//                        location.longitude.toFloat(),
//                        location.latitude.toFloat()
//                    )
//                }
//                try {
//                    if (response.isSuccessful){
//                        _markersMap.postValue(response.body())
//                    } else{
//                        Log.d(TAG, "response now Successful")
//                        _markersMap.postValue(null)
//                    }
//                } catch (e: HttpException) {
//                    Log.d(TAG, e.toString())
//                } catch (e: Throwable) {
//                    Log.d(TAG, e.toString())
//                }
//
//            }
        }
    }
}