package com.example.madcamp_week1_rev

import android.app.Activity
import android.app.Dialog
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
import android.net.Uri
import android.view.Window
import com.bumptech.glide.Glide

class GalleryFragment : Fragment() {

    private val PICK_IMAGE_REQUEST_CODE = 2000
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryImageAdapter
    private val imageList = mutableListOf<GalleryRecyclerModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView = view.findViewById(R.id.gallery_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = GalleryImageAdapter(imageList)
        recyclerView.adapter = adapter

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

        val addphotobtn: FloatingActionButton = view.findViewById(R.id.PhotoAddButton)
        addphotobtn.setOnClickListener{
            addphoto()
        }
        imageList.add(GalleryRecyclerModel(R.drawable.gallery))
        return view
    }

    private fun addphoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    } //onActivityResult랑 세트. PICK_IMAGE_REQUEST_CODE랑 비교해서 appphoto에서 온 실행이라는 것을 파악 가능

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null) {
                addImageToRecyclerView(selectedImageUri)
            }
        }
    }
    private fun addImageToRecyclerView(imageUri: Uri) {
        imageList.add(GalleryRecyclerModel(imageUri))
        adapter.notifyDataSetChanged()
    }

    private fun showImageDialog(image: Any) {
        val zoomableDialog = ZoomableImageDialog(requireContext(), image)
        zoomableDialog.show()
    }
  /*  private fun showImageDialog(image: Any) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bigimage)

        val imageView = dialog.findViewById<ImageView>(R.id.dialogImageView)
        Glide.with(requireContext()).load(image).into(imageView)
        dialog.show()
    }*/

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("삭제 확인")
        builder.setMessage("이미지를 삭제하시겠습니까?")

        builder.setPositiveButton("삭제") { _, _ ->
            (recyclerView.adapter as GalleryImageAdapter).removeItem(position)
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("취소") { _, _ ->
            // 사용자가 취소를 선택한 경우 아무것도 하지 않음
        }

        builder.show()
    }
}