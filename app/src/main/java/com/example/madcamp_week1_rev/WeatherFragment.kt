package com.example.madcamp_week1_rev

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date


class WeatherFragment : Fragment() {
    companion object {
        const val API_KEY: String = "02b795871d7cd0e1db5cdc9a3e411f25"
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F
      
        fun newInstance():WeatherFragment{
            return WeatherFragment()
        }
    }

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var weatherDescriptionLine: TextView
    private lateinit var currentDate: TextView
    private lateinit var currentTime: TextView
    private lateinit var minTemp: TextView
    private lateinit var maxTemp: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var reload : ImageButton
    private lateinit var weatherIconCard : CardView
    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var currentTempDiscription: TextView
    private lateinit var validation : MutableLiveData<Boolean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        cityName = view.findViewById(R.id.cityName)
        currentTemp = view.findViewById(R.id.currentTemp)
        weatherDescription = view.findViewById(R.id.weatherDescription)
        weatherDescriptionLine = view.findViewById(R.id.weatherDescriptionLine)
        currentDate = view.findViewById(R.id.currentDate)
        currentTime = view.findViewById(R.id.currentTime)
        minTemp = view.findViewById(R.id.minTemp)
        maxTemp = view.findViewById(R.id.maxTemp)
        weatherIcon = view.findViewById(R.id.weatherIcon)
        progressBar = view.findViewById(R.id.weatherLoading)
        reload = view.findViewById(R.id.reload_weather)
        weatherIconCard = view.findViewById(R.id.weatherIconCard)
        currentTempDiscription = view.findViewById(R.id.currentTempDescription)

        reload.setOnClickListener {
            initWhenReload()
        }
        return view
    }


    override fun onResume() {
        super.onResume()
        getWeatherInCurrentLocation()
    }

    private fun initWhenReload(){
        validation = weatherViewModel.getVal()
        Log.d("${validation.value}", "validation")
        progressBar.visibility = View.VISIBLE
        reload.visibility = View.INVISIBLE
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
                    validation = weatherViewModel.getVal()
                    updateWeather(weatherData)
                    validation.observe(viewLifecycleOwner, Observer {
                        if (validation.value==true){
                            progressBar.visibility = View.INVISIBLE
                            reload.visibility = View.VISIBLE
                            weatherIconCard.visibility = View.VISIBLE
                            currentTempDiscription.visibility = View.VISIBLE
                        }
                    })
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
        currentTemp.setText(weather.tempString+" ℃")
        weatherDescriptionLine.setText(weather.weatherType)
        weatherDescription.setText(weather.weatherType)
        val resourceID = resources.getIdentifier(weather.icon, "drawable", activity?.packageName)
        weatherIcon.setImageResource(resourceID)
        currentDate.setText(getCurrentDate())
        currentTime.setText(getCurrentTime())
        validation.value = true

    }

    override fun onPause() {
        super.onPause()
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener)
        }
    }

    private fun getCurrentDate(): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date(currentTime))
    }

    private fun getCurrentTime(): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return dateFormat.format(Date(currentTime))
    }
}