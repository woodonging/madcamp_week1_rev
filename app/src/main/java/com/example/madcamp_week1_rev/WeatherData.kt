package com.example.madcamp_week1_rev

import android.location.Geocoder
import org.json.JSONException
import org.json.JSONObject

class WeatherData{

    lateinit var tempString: String
    lateinit var tempmaxString: String
    lateinit var tempminString: String
    lateinit var icon: String
    lateinit var weatherType: String
    lateinit var latstring: String
    lateinit var lonstring: String
    private var weatherId: Int = 0

    fun fromJson(jsonObject: JSONObject?): WeatherData? {
        try{
            var weatherData = WeatherData()
            weatherData.weatherId = jsonObject?.getJSONArray("weather")?.getJSONObject(0)?.getInt("id")!!
            weatherData.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
            weatherData.icon = updateWeatherIcon(weatherData.weatherId)
            val lat: Double = (jsonObject.getJSONObject("coord").getDouble("lat"))
            val lon: Double = (jsonObject.getJSONObject("coord").getDouble("lon"))
            val roundedTemp: Double = (jsonObject.getJSONObject("main").getDouble("temp")-273.15)
            val roundedTempmax: Double = (jsonObject.getJSONObject("main").getDouble("temp_max")-273.15)
            val roundedTempmin: Double = (jsonObject.getJSONObject("main").getDouble("temp_min")-273.15)

            weatherData.latstring = lat.toString()
            weatherData.lonstring = lon.toString()
            weatherData.tempString = roundedTemp.toString()
            weatherData.tempmaxString = roundedTempmax.toString()
            weatherData.tempminString = roundedTempmin.toString()
            return weatherData
        }catch (e: JSONException){
            e.printStackTrace()
            return null
        }
    }

    private fun updateWeatherIcon(condition: Int): String {
        if (condition in 200..299) {
            return "thunderstorm"
        } else if (condition in 300..499) {
            return "rain" //lightrain
        } else if (condition in 500..599) {
            return "rain"
        } else if (condition in 600..700) {
            return "snow"
        } else if (condition in 701..771) {
            return "cloudy" //fog
        } else if (condition in 772..799) {
            return "cloudy" //overcast
        } else if (condition == 800) {
            return "clear"
        } else if (condition in 801..804) {
            return "cloudy"
        } else if (condition in 900..902) {
            return "thunderstorm"
        }
        if (condition == 903) {
            return "snow"
        }
        if (condition == 904) {
            return "clear"
        }
        return if (condition in 905..1000) {
            "thunderstorm"
        } else "dunno"

    }

}