package com.example.madcamp_week1_rev

import android.content.pm.PackageManager
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import java.io.File


class MainActivity : AppCompatActivity() {
    val PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val file = File(getExternalFilesDir(null), "test.txt")
        val contact: Fragment = ContactFragment()
        val gallery: Fragment = GalleryFragment()
        val weather: Fragment = WeatherFragment()
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, contact)
            .commit()

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
            ){
            var permissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INTERNET
            )
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_CODE)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab?.text) {
                    "Contact" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, contact)
                            .commit()
                    }

                    "Gallery" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, gallery)
                            .commit()
                    }

                    "Weather" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, weather)
                            .commit()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //tab.icon.setColorFilter(ContextCompat.getColor(this,@color))
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                for (grant in grantResults){
                    if (grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
                }
            }
        }
    }

}