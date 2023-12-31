package com.example.madcamp_week1_rev

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.madcamp_week1_rev.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    val PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contact: Fragment = ContactFragment()
        val gallery: Fragment = GalleryFragment()
        val memo: Fragment = MemoFragment()
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, contact)
            .commit()

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
            ){
            var permissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_CODE)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Contact" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, contact)
                            .commit()
                    }

                    "Gallery" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, gallery)
                            .commit()
                    }

                    "Memo" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.frame, memo)
                            .commit()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
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