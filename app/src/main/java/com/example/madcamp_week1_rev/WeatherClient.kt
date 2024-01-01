package com.example.madcamp_week1_rev

import com.google.gson.GsonBuilder
import retrofit2.Retrofit

object WeatherClient {
    private var instance: Retrofit? = null
    private  val gson = GsonBuilder().setLenient().create()


}