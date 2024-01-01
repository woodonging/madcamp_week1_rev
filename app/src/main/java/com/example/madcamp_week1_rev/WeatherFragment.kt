package com.example.madcamp_week1_rev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var city = "Seoul"
        val apikey = "2bf8bc56daf6323b543d99a2885461ed"
        var lang = "kr"

        var api = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apikey}&lang&units=metric"

/*        var result = requests.get(api)

        var data =*/

    }

}