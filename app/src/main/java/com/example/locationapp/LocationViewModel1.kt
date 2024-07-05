package com.example.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel1: ViewModel() {
    private val _location= mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    fun updateLocation(newLocation:LocationData){
        _location.value=newLocation
    }
}