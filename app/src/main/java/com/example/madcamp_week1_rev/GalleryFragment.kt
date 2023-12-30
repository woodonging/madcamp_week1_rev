package com.example.madcamp_week1_rev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.gallery_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 그리드의 열 수를 설정

        val imageList = generateDummyImages()
        val adapter = GalleryImageAdapter(imageList)
        recyclerView.adapter = adapter

        return view
    }

    private fun generateDummyImages(): ArrayList<GalleryRecyclerModel> {
        return arrayListOf(
            GalleryRecyclerModel(R.drawable.gallery),
            GalleryRecyclerModel(R.drawable.gallery),
            GalleryRecyclerModel(R.drawable.gallery),
            GalleryRecyclerModel(R.drawable.contact),
            GalleryRecyclerModel(R.drawable.contact),
            GalleryRecyclerModel(R.drawable.contact),
            GalleryRecyclerModel(R.drawable.memo),
            GalleryRecyclerModel(R.drawable.memo),
            GalleryRecyclerModel(R.drawable.memo),
            // 추가 이미지들...
        )
    }

}