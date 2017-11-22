package com.nawrot.mateusz.compass.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.MotionEvent
import com.nawrot.mateusz.compass.R


class ClearableEditText : AppCompatEditText {

    //@JvmOverloads constructor was giving strange effect, like no styles applied
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        //extra check with getDisplayedDrawable() to prevent setting icon visible when it already is, same thing with hiding when it is invisible
        if (!isTextEmpty() && getDisplayedDrawable() == null) {
            setClearIconVisible(true)
        } else if (isTextEmpty() && getDisplayedDrawable() != null) {
            setClearIconVisible(false)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (getDisplayedDrawable() != null && event != null && event.action == MotionEvent.ACTION_UP) {
            val leftBoundary = width - paddingRight - (getDisplayedDrawable()?.intrinsicWidth ?: 0)
            val rightBoundary = width

            val tapped = event.x >= leftBoundary && event.x <= rightBoundary

            if (tapped) {
                setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getDisplayedDrawable(): Drawable? {
        return compoundDrawables[2]
    }

    private fun setClearIconVisible(visible: Boolean) {
        val drawable = if (visible) R.drawable.ic_clear else 0
        setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    private fun isTextEmpty(): Boolean {
        return text.isEmpty()
    }

}