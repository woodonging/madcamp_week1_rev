package com.example.madcamp_week1_rev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GalleryImageAdapter(private val imageList: ArrayList<GalleryRecyclerModel>):
    RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.gallery_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item_recyclerview, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageModel = imageList[position]
        holder.imageView.setImageResource(imageModel.image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }





}