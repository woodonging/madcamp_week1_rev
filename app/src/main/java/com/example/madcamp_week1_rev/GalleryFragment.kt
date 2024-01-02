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
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GalleryFragment : Fragment() {

    private var currentPhotoPath: String? = null
    private val addFromGalleryCode = 100
    private val addByCameraCode = 200
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryImageAdapter
    private lateinit var imageCount : TextView
    private lateinit var menuButton: AppCompatButton
    private lateinit var gallerybutton: ImageButton
    private lateinit var camerabutton: ImageButton
    private lateinit var galleryViewModel: GalleryViewModel
    private var isMenuOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryViewModel=ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
    }

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

        adapter = GalleryImageAdapter(galleryViewModel.getImageList())
        recyclerView.adapter = adapter
        imageCount = view.findViewById(R.id.imageCount)

        adapter.setItemClickListener(object: GalleryImageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                showImageDialog(galleryViewModel.getImage(position).image)
            }
        })

        adapter.setItemLongClickListener(object : GalleryImageAdapter.OnItemLongClickListener {
            override fun onLongClick(position: Int) {
                showDeleteDialog(position)
            }
        })

        menuButton = view.findViewById(R.id.menuButton)
        gallerybutton = view.findViewById(R.id.PhotoAddButton)
        camerabutton = view.findViewById(R.id.CameraButton)

        menuButton.setOnClickListener {
            if (!isMenuOpen){
                menuButton.setBackgroundResource(R.drawable.minus_symbol)
                gallerybutton.visibility = View.VISIBLE
                camerabutton.visibility = View.VISIBLE
                isMenuOpen=true
            } else {
                menuButton.setBackgroundResource(R.drawable.plus_symbol)
                gallerybutton.visibility = View.INVISIBLE
                camerabutton.visibility = View.INVISIBLE
                isMenuOpen=false
            }
        }

        gallerybutton.setOnClickListener{
            addFromGallery()
        }
        camerabutton.setOnClickListener{
            addByCamera()
        }

        countImage()
        return view
    }

    private fun countImage(){
            imageCount.setText("이미지 "+galleryViewModel.getImageList().size+" 개")
    }

    private fun addFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, addFromGalleryCode)
    } //onActivityResult랑 세트. addfromgallery_code랑 비교해서 addfromgallery에서 온 실행이라는 것을 파악 가능

    private fun addByCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 이미지를 저장할 파일 생성
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        // 파일이 정상적으로 생성되면 카메라 앱에 파일을 저장할 수 있는 URI를 제공
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.madcamp_week1_rev.fileprovider",
                it
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, addByCameraCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addFromGalleryCode && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null) {
                addImageToRecyclerView(selectedImageUri)
            }
        }

        if (requestCode == addByCameraCode && resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let { path ->
                val imageFile = File(path)
                addImageToRecyclerView(Uri.fromFile(imageFile))
            }
        }

    }
    private fun addImageToRecyclerView(image: Any) {
        galleryViewModel.addImage(GalleryRecyclerModel(image))
        adapter.notifyDataSetChanged()
        countImage()
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
            countImage()
        }

        builder.setNegativeButton("취소") { _, _ ->
            // 사용자가 취소를 선택한 경우 아무것도 하지 않음
        }

        builder.show()
    }

    private fun createImageFile(): File {
        try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            // 파일 경로 저장
            currentPhotoPath = imageFile.absolutePath
            return imageFile
        } catch (ex: IOException) {
            Log.e("GalleryFragment", "Error creating image file", ex)
            throw ex
        }
    }

    companion object{
        fun newInstance():GalleryFragment{
            return GalleryFragment()
        }
    }
}