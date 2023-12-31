package com.example.madcamp_week1_rev

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.net.Uri
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.TextView

class GalleryFragment : Fragment() {

    private val addfromgallerycode = 100
    private val addbycameracode = 200
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryImageAdapter
    private lateinit var emptyview: TextView
    private lateinit var gallerybutton: FloatingActionButton
    private lateinit var camerabutton: FloatingActionButton
    private val imageList = mutableListOf<GalleryRecyclerModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView = view.findViewById(R.id.gallery_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        val itemDecoration = GalleryImageSpacing(spacingInPixels)
        recyclerView.addItemDecoration(itemDecoration)

        adapter = GalleryImageAdapter(imageList)
        recyclerView.adapter = adapter
        emptyview = view.findViewById(R.id.emptygallery)

        adapter.setItemClickListener(object: GalleryImageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                showImageDialog(imageList[position].image)
            }
        })

        adapter.setItemLongClickListener(object : GalleryImageAdapter.OnItemLongClickListener {
            override fun onLongClick(position: Int) {
                showDeleteDialog(position)
            }
        })

        gallerybutton = view.findViewById(R.id.PhotoAddButton)
        camerabutton = view.findViewById(R.id.CameraButton)

        gallerybutton.setOnClickListener{
            addfromgallery()
        }
        camerabutton.setOnClickListener{
            addbycamera()
        }

        isempty()
        return view
    }

    private fun isempty(){
        if (imageList.size<=0) {
            emptyview.visibility = View.VISIBLE // 표시
        } else {
            emptyview.visibility = View.GONE // 숨김
        }
    }

    private fun addfromgallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, addfromgallerycode)
    } //onActivityResult랑 세트. addfromgallery_code랑 비교해서 addfromgallery에서 온 실행이라는 것을 파악 가능

    private fun addbycamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, addbycameracode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addfromgallerycode && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null) {
                addImageToRecyclerView(selectedImageUri)
            }
        }

        if (requestCode == addbycameracode && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            if (imageBitmap != null) {
                addImageToRecyclerView(imageBitmap)
            }
        }

    }
    private fun addImageToRecyclerView(image: Any) {
        imageList.add(GalleryRecyclerModel(image))
        adapter.notifyDataSetChanged()
        isempty()
    }

    private fun showImageDialog(image: Any) {
        val zoomableDialog = ZoomableImageDialog(requireContext(), image)
        zoomableDialog.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("삭제 확인")
        builder.setMessage("이미지를 삭제하시겠습니까?")

        builder.setPositiveButton("삭제") { _, _ ->
            (recyclerView.adapter as GalleryImageAdapter).removeItem(position)
            adapter.notifyDataSetChanged()
            isempty()
        }

        builder.setNegativeButton("취소") { _, _ ->
            // 사용자가 취소를 선택한 경우 아무것도 하지 않음
        }

        builder.show()
    }
}