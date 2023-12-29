package com.example.madcamp_week1_rev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.madcamp_week1_rev.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
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
}