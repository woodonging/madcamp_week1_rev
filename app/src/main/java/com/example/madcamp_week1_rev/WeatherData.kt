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
            val roundedTemp: Int = (jsonObject.getJSONObject("main").getDouble("temp")-273.15).toInt()
            val roundedTempmax: Int = (jsonObject.getJSONObject("main").getDouble("temp_max")-273.15).toInt()
            val roundedTempmin: Int = (jsonObject.getJSONObject("main").getDouble("temp_min")-273.15).toInt()

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
            return "weather_thunderstorm"
        } else if (condition in 300..499) {
            return "weather_rain" //lightrain
        } else if (condition in 500..599) {
            return "weather_rain"
        } else if (condition in 600..700) {
            return "weather_snow"
        } else if (condition in 701..771) {
            return "weather_fog"
        } else if (condition in 772..799) {
            return "weather_overcast"
        } else if (condition == 800) {
            return "weather_sunny"
        } else if (condition in 801..804) {
            return "weather_cloudy"
        } else if (condition in 900..902) {
            return "weather_thunderstorm"
        }
        if (condition == 903) {
            return "weather_snow"
        }
        if (condition == 904) {
            return "weather_sunny"
        }
        return if (condition in 905..1000) {
            "weather_thunderstorm"
        } else "dunno"

    }

}