package com.example.madcamp_week1_rev

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class WeatherFragment : Fragment() {
    companion object {
        const val API_KEY: String = "02b795871d7cd0e1db5cdc9a3e411f25"
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F

    }

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var currentDate: TextView
    private lateinit var currentTime: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var reload : AppCompatButton
    private lateinit var weatherDetailShow : AppCompatButton
    private lateinit var weatherDetailHide : AppCompatButton
    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var validation : MutableLiveData<Boolean>
    private lateinit var background : ConstraintLayout
    private lateinit var weatherPop: ConstraintLayout
    private lateinit var weatherDescription : TextView
    private lateinit var detailShow : ConstraintLayout
    private lateinit var base : ConstraintLayout
    private lateinit var humidityText : TextView
    private lateinit var feelsLikeText : TextView
    private lateinit var windSpeedText : TextView

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
        currentDate = view.findViewById(R.id.currentDate)
        currentTime = view.findViewById(R.id.currentTime)
        weatherIcon = view.findViewById(R.id.weatherIcon)
        progressBar = view.findViewById(R.id.weatherLoading)
        reload = view.findViewById(R.id.reloadWeather)
        background = view.findViewById(R.id.weatherView)
        weatherPop = view.findViewById(R.id.weatherPop)
        weatherDescription = view.findViewById(R.id.weatherDescription)
        detailShow = view.findViewById(R.id.detailShow)
        base = view.findViewById(R.id.base)
        weatherDetailShow = view.findViewById(R.id.weatherDetailShow)
        weatherDetailHide = view.findViewById(R.id.weatherDetailHide)
        humidityText = view.findViewById(R.id.humidity)
        windSpeedText = view.findViewById(R.id.wind)
        feelsLikeText = view.findViewById(R.id.feelTemp)

        getWeatherInCurrentLocation()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reload.setOnClickListener {
            initWhenReload()
        }
        weatherDetailShow.setOnClickListener{
            weatherDetailShow()

        }
        weatherDetailHide.setOnClickListener{
            weatherDetailHide()
        }

    }


    private fun weatherDetailShow(){
        val layoutParams = weatherPop.layoutParams
        val newHeight = resources.getDimensionPixelSize(R.dimen.new_height)
        layoutParams.height = newHeight
        weatherPop.layoutParams = layoutParams
        weatherPop.requestLayout()
        detailShow.visibility = View.VISIBLE
        weatherDetailShow.visibility = View.GONE
        weatherDetailHide.visibility = View.VISIBLE
    }
    private fun weatherDetailHide(){
        val layoutParams = weatherPop.layoutParams
        val newHeight = resources.getDimensionPixelSize(R.dimen.prev_height)
        layoutParams.height = newHeight
        weatherPop.layoutParams = layoutParams
        weatherPop.requestLayout()
        detailShow.visibility = View.GONE
        weatherDetailShow.visibility = View.VISIBLE
        weatherDetailHide.visibility = View.GONE
    }

    private fun initWhenReload(){
        weatherViewModel.resetFalse()
        progressBar.visibility = View.VISIBLE
        reload.visibility = View.GONE
        currentDate.visibility = View.GONE
        currentTime.visibility = View.GONE
        currentTemp.visibility = View.GONE
        cityName.visibility = View.GONE
        weatherIcon.visibility = View.GONE
        background.visibility = View.GONE
        detailShow.visibility = View.GONE
        weatherDetailShow.visibility = View.GONE
        weatherDetailHide.visibility = View.GONE
        getWeatherInCurrentLocation()
    }

    private fun getWeatherInCurrentLocation(){

        progressBar.visibility = View.VISIBLE
        reload.visibility = View.GONE
        currentDate.visibility = View.GONE
        currentTime.visibility = View.GONE
        currentTemp.visibility = View.GONE
        cityName.visibility = View.GONE
        weatherIcon.visibility = View.GONE
        background.visibility = View.INVISIBLE
        detailShow.visibility = View.GONE
        weatherDetailShow.visibility = View.GONE
        weatherDetailHide.visibility = View.GONE

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
                        if (weatherViewModel.getVal().value == true){
                            reload.visibility = View.VISIBLE
                            weatherIcon.visibility = View.VISIBLE
                            currentDate.visibility = View.VISIBLE
                            currentTemp.visibility = View.VISIBLE
                            currentTime.visibility = View.VISIBLE
                            cityName.visibility = View.VISIBLE
                            background.visibility = View.VISIBLE
                            weatherDetailShow.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
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
            cityName.text = city
        }

        currentTemp.text = weather.tempString+"℃"

        val resourceID = resources.getIdentifier(weather.icon, "drawable", activity?.packageName)
        val time = getCurrentTime()
        val hour = time.split(" ")[1].split(":")[0].toInt()
        var imageId = resources.getIdentifier(weather.updateWeatherImage(hour), "drawable",activity?.packageName)

        weatherViewModel.getImageSrc().observe(viewLifecycleOwner, Observer{
            imageSrc -> background.setBackgroundResource(imageId)
        })
        weatherViewModel.updateBackground(imageId)

        val date = getCurrentDate().split("-")
        val dayString = when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> ""
        }

        weatherIcon.setImageResource(resourceID)
        currentDate.text = "${date[0]}/${date[1]}/${date[2]}"
        currentTime.text = "$dayString   ${time.split(" ")[1]}"
        weatherDescription.text = weather.weatherDescription


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