package com.example.madcamp_week1_rev

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryImageAdapter(private val imageList: MutableList<GalleryRecyclerModel>):
    RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {

    private var itemClickListener : OnItemClickListener? = null
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

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
        if (imageModel.image is Int) {
            Glide.with(holder.itemView.context)
                .load(imageModel.image)
                .into(holder.imageView)
        } else if (imageModel.image is Uri) {
            Glide.with(holder.itemView.context)
                .load(imageModel.image)
                .into(holder.imageView)
        }
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

}