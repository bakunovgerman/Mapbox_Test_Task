package com.example.mapbox_test_task.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox_test_task.App
import com.example.mapbox_test_task.model.data.MarkersMap
import com.example.mapbox_test_task.model.MarkersMapModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    companion object{
        private const val TAG = "viewModelLog"
    }

    private val errorHandler = CoroutineExceptionHandler { _, error ->
        Log.d("errorHandler", error.toString())
    }

    // init LiveData
    val markersMap: LiveData<MarkersMap?> get() = _markersMap
    private val _markersMap = MutableLiveData<MarkersMap?>()

    // init Model
    private val markersMapModel: MarkersMapModel = MarkersMapModel(App.applicationContext)

    fun loadMarkersMap(location: Location) {
        val viewModelScopeCustom = viewModelScope + Dispatchers.Main
        viewModelScopeCustom.launch(errorHandler) {
            var response: Response<MarkersMap>
            withContext(Dispatchers.IO) {
                response = markersMapModel.getMarkersMapAPI(
                    location.longitude.toFloat(),
                    location.latitude.toFloat()
                )
            }
            try {
                if (response.isSuccessful){
                    _markersMap.postValue(response.body())
                } else{
                    Log.d(TAG, "response now Successful")
                    _markersMap.postValue(null)
                }
            } catch (e: HttpException) {
                Log.d(TAG, e.toString())
            } catch (e: Throwable) {
                Log.d(TAG, e.toString())
            }
        }
    }
}