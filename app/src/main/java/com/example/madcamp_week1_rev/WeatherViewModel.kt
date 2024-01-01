package com.example.madcamp_week1_rev

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices

class WeatherViewModel : ViewModel() {
    private var lon : Double = 3.14
    private var lat : Double = 3.14

    fun getInfo():Pair<Double, Double>{
        return Pair(lon,lat)
    }
    @SuppressLint("MissingPermission")
    fun getFirstLocation(context: Context){
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.lastLocation.
        addOnSuccessListener { success: Location? ->
            success?.let { location ->
                lat=location.latitude
                lon=location.longitude
            }
        }
            .addOnFailureListener{fail ->
                Toast.makeText(context,"위치 정보 불러오기 실패", Toast.LENGTH_SHORT)
            }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(context:Context){
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.getCurrentLocation(100,null).
        addOnSuccessListener { success: Location? ->
            success?.let { location ->
                lat=location.latitude
                lon=location.longitude
            }
        }
            .addOnFailureListener{fail ->
                Toast.makeText(context,"위치 정보 불러오기 실패", Toast.LENGTH_SHORT)
            }
    }
}