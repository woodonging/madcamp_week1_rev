package com.example.madcamp_week1_rev

import android.widget.ImageView
import androidx.lifecycle.ViewModel

class GalleryViewModel: ViewModel() {
    private val imageList = mutableListOf<GalleryRecyclerModel>()

    fun getImageList(): MutableList<GalleryRecyclerModel>{
        return imageList
    }
    fun getImage(position: Int): GalleryRecyclerModel{
        return imageList[position]
    }
    fun addImage(image: GalleryRecyclerModel){
        imageList.add(image)
    }
}
