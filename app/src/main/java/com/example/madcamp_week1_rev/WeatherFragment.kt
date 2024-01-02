package com.example.madcamp_week1_rev

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import okhttp3.internal.http2.Header
import org.json.JSONObject
import java.util.Locale


class WeatherFragment : Fragment() {
    companion object {
        const val API_KEY: String = "02b795871d7cd0e1db5cdc9a3e411f25"
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F
        const val WEATHER_REQUEST: Int = 102
      
        fun newInstance():WeatherFragment{
            return WeatherFragment()
        }
    }

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var minTemp: TextView
    private lateinit var maxTemp: TextView
    private lateinit var weatherIcon: ImageView


    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var validation = weatherViewModel.getVal()
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        cityName = view.findViewById(R.id.cityName)
        currentTemp = view.findViewById(R.id.currentTemp)
        weatherDescription = view.findViewById(R.id.weatherDescription)
        minTemp = view.findViewById(R.id.minTemp)
        maxTemp = view.findViewById(R.id.maxTemp)
        weatherIcon = view.findViewById(R.id.weatherIcon)

        return view
    }


    override fun onResume() {
        super.onResume()
        getWeatherInCurrentLocation()
    }

    private fun getWeatherInCurrentLocation(){
        mLocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = LocationListener { p0 ->
            val params: RequestParams = RequestParams()
            params.put("lat", p0.latitude)
            params.put("lon", p0.longitude)
            params.put("appid", Companion.API_KEY)
            doNetworking(params)
        }


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener)
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener)
    }


    private fun doNetworking(params: RequestParams) {
        var client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object: JsonHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                response: JSONObject?
            ) {
                val weatherData = WeatherData().fromJson(response)
                if (weatherData != null) {
                    updateWeather(weatherData)
                }
            }

        })
    }

    private fun updateWeather(weather: WeatherData) {

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(weather.latstring.toDouble() ?: 0.0, weather.lonstring.toDouble() ?: 0.0, 1)
        if (addresses != null) {
            val city = addresses[0]?.adminArea //subLocality로 하면 유성구로 출력 현재는 대전광역시
            cityName.setText(city)
        }

        minTemp.setText("Min :"+weather.tempminString+" ℃")
        maxTemp.setText("Max :"+weather.tempmaxString+" ℃")
        currentTemp.setText("Current :"+weather.tempString+" ℃")
        weatherDescription.setText(weather.weatherType)
        val resourceID = resources.getIdentifier(weather.icon, "drawable", activity?.packageName)
        weatherIcon.setImageResource(resourceID)
    }

    override fun onPause() {
        super.onPause()
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener)
        }
    }
}