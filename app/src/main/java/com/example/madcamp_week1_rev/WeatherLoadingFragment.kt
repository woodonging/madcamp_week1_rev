package com.example.madcamp_week1_rev

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class WeatherLoadingFragment : Fragment() {
    private lateinit var weatherViewModel: WeatherViewModel
    private var validation = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingText = view.findViewById<ProgressBar>(R.id.weatherLoading)
        validation = weatherViewModel.getVal()
        Log.d("${validation.value}","validation check")
        validation.observe(viewLifecycleOwner, Observer {
            weatherViewModel.getLocation(requireContext())
            if (validation.value==true){
                val weather = WeatherFragment.newInstance()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, weather)
                transaction.commit()
                }
        })
    }


    companion object{
        fun newInstance(): WeatherLoadingFragment
        {
            return WeatherLoadingFragment()
        }
    }
}