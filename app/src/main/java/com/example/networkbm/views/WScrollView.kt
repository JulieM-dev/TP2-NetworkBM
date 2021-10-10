package com.example.networkbm.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ScrollView

class WScrollView(context: Context, attributeSet: AttributeSet) : HorizontalScrollView(context, attributeSet) {

    lateinit var sv: ScrollView
    lateinit var ecran: ImageView

    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        sv.onTouchEvent(ev)
        super.onTouchEvent(ev)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        sv.onInterceptTouchEvent(ev)
        super.onInterceptTouchEvent(ev)
        return true
    }

}