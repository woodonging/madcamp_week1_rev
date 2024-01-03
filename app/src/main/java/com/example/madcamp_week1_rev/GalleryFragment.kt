package com.example.madcamp_week1_rev

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.madcamp_week1_rev.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private lateinit var binding: FragmentGalleryBinding

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
    private var areButtonsVisible = false

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

        gallerybutton.setOnClickListener{
            addFromGallery()
        }
        camerabutton.setOnClickListener{
            addByCamera()
        }

        areButtonsVisible=false

        countImage()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGalleryBinding.bind(view)
        binding.menuButton.setOnClickListener {

            TransitionManager.beginDelayedTransition(binding?.galleryfragment)

            if (areButtonsVisible) {
                // 버튼 숨기기
                menuButton.setBackgroundResource(R.drawable.plus_symbol)
                animateTranslationX(gallerybutton, 0f, gallerybutton.width.toFloat())
                animateTranslationX(camerabutton, 0f, camerabutton.width.toFloat())
                animateAlpha(gallerybutton, 1f, 0f)
                animateAlpha(camerabutton, 1f, 0f)

            } else {
                // 버튼 나타내기
                menuButton.setBackgroundResource(R.drawable.minus_symbol)
                animateTranslationX(gallerybutton, gallerybutton.width.toFloat(),0f)
                animateTranslationX(camerabutton, camerabutton.width.toFloat(), 0f)
                animateAlpha(gallerybutton, 0f, 1f)
                animateAlpha(camerabutton, 0f, 1f)
            }


            /*val fadeIn = ObjectAnimator.ofFloat(menuButton, "alpha", 0f, 1f)
            fadeIn.duration = 150

            val fadeOut = ObjectAnimator.ofFloat(menuButton, "alpha", 1f, 0f)
            fadeOut.duration = 150

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(fadeOut,fadeIn)
            animatorSet.start()*/

            // 버튼 가시성 상태 변경
            areButtonsVisible = !areButtonsVisible

        }
    }

    private fun animateAlpha(view: View?, fromAlpha: Float, toAlpha: Float) {
        view?.let {
            val animator = ObjectAnimator.ofFloat(it, "alpha", fromAlpha, toAlpha)
            animator.interpolator = LinearInterpolator()
            animator.duration = 300 // 애니메이션 지속 시간 (밀리초)
            animator.start()
        }
    }

    private fun animateTranslationX(view: View?, fromX: Float, toX: Float) {
        view?.let {
            val animator = ValueAnimator.ofFloat(fromX, toX)
            animator.interpolator = LinearInterpolator()
            animator.duration = 300 // 애니메이션 지속 시간 (밀리초)
            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                it.translationX = value
            }
            animator.start()
        }
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