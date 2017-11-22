package com.nawrot.mateusz.compass.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView


class RotationImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private var currentAngle: Float = 0f

    override fun onDraw(canvas: Canvas?) {
        pivotX = (width / 2).toFloat()
        pivotY = (height / 2).toFloat()
        rotation = currentAngle
        super.onDraw(canvas)
    }

    fun rotateImage(angle: Float) {
        currentAngle = angle
        invalidate()
    }
}