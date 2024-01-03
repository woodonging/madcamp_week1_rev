package com.example.madcamp_week1_rev

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class WeatherViewModel : ViewModel() {

    private var validation : MutableLiveData<Boolean> = MutableLiveData(false)
    private var imageSrc = MutableLiveData<Int>()

    fun getVal():MutableLiveData<Boolean>{
        return validation
    }
    fun getImageSrc():MutableLiveData<Int>{
        return imageSrc
    }
    fun updateBackground(imageId:Int){
        imageSrc.value = imageId
        validation.value = true
    }

    fun resetFalse(){
        validation.value = false
    }

}