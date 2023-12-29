package com.example.madcamp_week1_rev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.madcamp_week1_rev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, ContactFragment())
            .commit()

        binding.bottomNavi.setOnItemSelectedListener{
            item -> when(item.itemId){
                R.id.contactFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ContactFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.galleryFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, GalleryFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.memoFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, MemoFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }
}