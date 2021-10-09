package com.example.networkbm.models

import android.content.Context
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.ScrollView

class WScrollView(context: Context) : HorizontalScrollView(context) {

    lateinit var sv: ScrollView


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        var ret : Boolean = super.onTouchEvent(ev)
        ret = ret || sv.onTouchEvent(ev)
        return ret
    }



}