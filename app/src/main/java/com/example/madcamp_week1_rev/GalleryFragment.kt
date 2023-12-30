package com.example.madcamp_week1_rev

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton



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
        val addphotobtn: FloatingActionButton = view.findViewById(R.id.PhotoAddButton)
        recyclerView.adapter = adapter

        addphotobtn.setOnClickListener{
            addphoto()
        }

        adapter.setItemClickListener(object: GalleryImageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(context, "메시지 내용", Toast.LENGTH_SHORT).show()
                //클릭했을 때 이미지를 크게 보는 다이얼로그를 띄워주는 이벤트 넣어주기
            }
        })

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

    private fun addphoto() {
        Toast.makeText(context, "이미지 추가 버튼 작동", Toast.LENGTH_SHORT).show()
    }
}