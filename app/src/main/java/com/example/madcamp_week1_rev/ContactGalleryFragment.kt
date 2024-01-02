package com.example.madcamp_week1_rev

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContactGalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryImageAdapter
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryViewModel=ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
        contactViewModel=ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_gallery, container, false)

        recyclerView = view.findViewById(R.id.gallery_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        val itemDecoration = GalleryImageSpacing(spacingInPixels)
        recyclerView.addItemDecoration(itemDecoration)

        adapter = GalleryImageAdapter(galleryViewModel.getImageList())
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactPosition = arguments?.getInt("contactPosition")
        adapter.setItemClickListener(object: GalleryImageAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                contactViewModel.setImage(contactPosition!! ,galleryViewModel.getImage(position).image)
                requireActivity().supportFragmentManager.popBackStack()
            }
        })

    }

    companion object{
        fun newInstance(contactPosition: Int):ContactGalleryFragment{
            val fragment = ContactGalleryFragment()
            val args = Bundle()
            args.putInt("contactPosition", contactPosition)
            fragment.arguments = args
            return fragment
        }
    }
}