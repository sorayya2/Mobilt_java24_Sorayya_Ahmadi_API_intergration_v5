package com.example.weatherapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _Jsondata = MutableStateFlow<Jsonresponse?>(null)
    val Jsondata: StateFlow<Jsonresponse?> = _Jsondata
    private val weatherapi = APIinterface.create()

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = weatherapi.getWeather(city, apiKey)
                _Jsondata.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _Jsondata.value = null
            }
        }
    }

    fun fetchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = weatherapi.getWeatherByLocation(latitude, longitude, apiKey)
                _Jsondata.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _Jsondata.value = null
            }
        }
    }

}