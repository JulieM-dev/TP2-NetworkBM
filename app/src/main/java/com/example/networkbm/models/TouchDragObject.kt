package com.example.networkbm.models

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class TouchDragObject(private var rootLayout: ViewGroup, private var posX: Int?,
                      private var posY: Int?
) : View.OnTouchListener {

    override fun onTouch(objet: Objet, event: MotionEvent): Boolean {
        val X = event.rawX
        val Y = event.rawY
        if(posX == null && posY == null){
            posX = 625
            posY = 1080
        }
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                val lParams : RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
                posX = (X - lParams.leftMargin).toInt()
                posY = (Y - lParams.topMargin).toInt()
                view.layoutParams = lParams
            }
            MotionEvent.ACTION_UP -> {
            }git p
            MotionEvent.ACTION_POINTER_DOWN -> {
            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val lParams : RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin = (X - posX!!).toInt()
                lParams.topMargin = (Y - posY!!).toInt()
                lParams.rightMargin = 0
                lParams.bottomMargin = 0
                view.layoutParams = lParams
                var objet = view as Objet



            }
        }
        rootLayout.invalidate()
        return true
    }
}