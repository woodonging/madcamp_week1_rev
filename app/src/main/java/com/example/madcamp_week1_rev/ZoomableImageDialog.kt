package com.example.madcamp_week1_rev

import android.app.Dialog
import android.content.Context
import android.graphics.Outline
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewOutlineProvider
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class ZoomableImageDialog(context: Context, private val imageUrl: Any) : Dialog(context) {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bigimage)

        imageView = findViewById(R.id.dialogImageView)
        Glide.with(context).load(imageUrl).into(imageView)


        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.decorView?.findViewById<ImageView>(R.id.dialogImageView)?.apply {
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(0, 0, view?.width ?: 0, view?.height ?: 0, 30.0f)
                }
            }
        }

        val closeButton: AppCompatButton = findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dismiss()
        }
    }
}