package com.example.networkbm.models

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class ChoiceTouchListener(private var rootLayout: ViewGroup) : View.OnTouchListener {

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        var X = event.rawX
        var Y = event.rawY
        var xDelta = 625
        var yDelta = 1200
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                val lParams : RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
                xDelta = (X - lParams.leftMargin).toInt()
                yDelta = (Y - lParams.topMargin).toInt()

                view.layoutParams = lParams
            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_POINTER_DOWN -> {

            }
            MotionEvent.ACTION_POINTER_UP -> {

            }
            MotionEvent.ACTION_MOVE -> {
                val lParams : RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin = (X - xDelta).toInt()
                lParams.topMargin = (Y - yDelta).toInt()
                lParams.rightMargin = 0
                lParams.bottomMargin = 0

                view.layoutParams = lParams
            }
        }
        rootLayout.invalidate()
        return true
    }
}