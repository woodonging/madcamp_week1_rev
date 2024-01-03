package com.example.madcamp_week1_rev

import org.json.JSONException
import org.json.JSONObject
import kotlin.random.Random

class WeatherData{

    lateinit var tempString: String
    lateinit var tempmaxString: String
    lateinit var tempminString: String
    lateinit var weatherDescription: String
    lateinit var icon: String
    lateinit var weatherType: String
    lateinit var humidity : String
    lateinit var feels_like : String
    lateinit var windspeed : String
    lateinit var latstring: String
    lateinit var lonstring: String
    private var weatherId: Int = 0

    fun fromJson(jsonObject: JSONObject?): WeatherData? {
        try{
            var weatherData = WeatherData()
            weatherData.weatherId = jsonObject?.getJSONArray("weather")?.getJSONObject(0)?.getInt("id")!!
            weatherData.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
            weatherData.icon = updateWeatherIcon(weatherData.weatherId)
            weatherData.weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
            weatherData.humidity = jsonObject.getJSONObject("main").getString("humidity")
            weatherData.feels_like = jsonObject.getJSONObject("main").getString("feels_like")
            weatherData.windspeed = jsonObject.getJSONObject("wind").getString("speed")

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
        when (condition) {
            in 200..299 -> {
                return "weather_thunderstorm"
            }
            in 300..499 -> {
                return "weather_rain" //lightrain
            }
            in 500..599 -> {
                return "weather_rain"
            }
            in 600..700 -> {
                return "weather_snow"
            }
            in 701..771 -> {
                return "weather_fog"
            }
            in 772..799 -> {
                return "weather_cloudy"
            }
            800 -> {
                return "weather_sunny"
            }
            in 801..804 -> {
                return "weather_cloudy"
            }
            in 900..902 -> {
                return "weather_thunderstorm"
            }
            903 -> {
                return "weather_snow"
            }
            904 -> {
                return "weather_sunny"
            }
            else -> return if (condition in 905..1000) {
                "weather_thunderstorm"
            } else "dunno"
        }

    }

    fun updateWeatherImage(time: Int): String {
        val number = Random.nextInt(5)
        val timer = when (time) {
                in 6 .. 9 -> {
                    "morning"
                }
                in 10 .. 17 -> {
                    "noon"
                }
                in 18 .. 21 -> {
                    "evening"
                }
                else -> "night"
        }
//        return "$timer$number"
        return "${timer}${number}"
    }

}