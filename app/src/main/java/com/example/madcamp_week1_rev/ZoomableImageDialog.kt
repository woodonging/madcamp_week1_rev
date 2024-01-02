package com.example.madcamp_week1_rev

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide

class ZoomableImageDialog(context: Context, private val imageUrl: Any) : Dialog(context) {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var scaleFactor = 1.0f
    private var dialogWidth = 0
    private var dialogHeight = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_bigimage)

        imageView = findViewById(R.id.dialogImageView)
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())

        // 이미지 로딩
        Glide.with(context).load(imageUrl).into(imageView)

        val closeButton: ImageButton = findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dismiss() // 다이얼로그를 닫습니다.
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // ScaleGestureDetector 및 GestureDetector로부터 이벤트 전달
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // 줌인 및 줌아웃을 수행
            scaleFactor *= detector.scaleFactor
            scaleFactor = if (scaleFactor < 1.0f) 1.0f else scaleFactor // 최소 스케일 제한
            scaleFactor = if (scaleFactor > 3.0f) 3.0f else scaleFactor // 최대 스케일 제한

            // 이미지뷰에 스케일 적용
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor

            return true
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // 다이얼로그의 크기를 가져옴
        dialogWidth = window?.decorView?.width ?: 0
        dialogHeight = window?.decorView?.height ?: 0
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent): Boolean {
            // 더블탭하면 처음의 상태로 돌아감
            scaleFactor = 1.0f
            imageView.translationX = 0f
            imageView.translationY = 0f
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            // 핀치 줌 상태에서의 이동 처리
            if (scaleFactor > 1.0f) {
                // 이미지뷰의 translationX 및 translationY에 이동량을 더해줌
                imageView.translationX -= distanceX
                imageView.translationY -= distanceY
            }
            return true
        }
    }
}