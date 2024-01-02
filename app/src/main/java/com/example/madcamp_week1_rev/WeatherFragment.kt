package com.example.madcamp_week1_rev

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
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


class WeatherFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_weather, container, false)
        val cityName:TextView = view.findViewById(R.id.cityName)
        val currentTemp:TextView = view.findViewById(R.id.currentTemp)
        val weatherDescription:TextView= view.findViewById(R.id.weatherDescription)
        val minMaxTemp:TextView= view.findViewById(R.id.minMaxTemp)
        val weatherIcon: ImageView =view.findViewById(R.id.weatherIcon)

        var validation = weatherViewModel.getVal()

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
    companion object{
        fun newInstance():WeatherFragment{
            return WeatherFragment()
        }
    }

}
