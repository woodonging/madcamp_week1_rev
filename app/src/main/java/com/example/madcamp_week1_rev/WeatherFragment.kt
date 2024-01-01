package com.example.madcamp_week1_rev

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import android.util.Log
import java.util.Locale


class WeatherFragment : Fragment() {

    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        val cityName: TextView = view.findViewById(R.id.cityName)
        val currentTemp: TextView = view.findViewById(R.id.currentTemp)
        val weatherDescription: TextView = view.findViewById(R.id.weatherDescription)
        val minMaxTemp: TextView = view.findViewById(R.id.minMaxTemp)
        val weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)

        getLocation(cityName)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var city = "Seoul"
        val apikey = "2bf8bc56daf6323b543d99a2885461ed"
        var lang = "kr"

        var weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apikey}&lang&units=metric"
        val MIN_TIME: Long = 5000
        val MIN_DISTANCE: Float = 1000F
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(cityName: TextView) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.getCurrentLocation(100, null)
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    lat = location.latitude
                    lon = location.longitude

                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    Log.d("addresses 이전","$lat")
                    val addresses = geocoder.getFromLocation(lat ?: 0.0, lon ?: 0.0, 1)
                    Log.d("addresses 이후","$lat")
                    if (addresses != null) {
                        val city = addresses[0]?.adminArea //subLocality로 하면 유성구로 출력 현재는 대전광역시
                        cityName.text = city ?: "Unknown City"
                    }
                }
            }
            .addOnFailureListener { fail ->
                Toast.makeText(context, "위치 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }
}