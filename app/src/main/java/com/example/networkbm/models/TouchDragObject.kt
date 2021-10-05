package com.example.networkbm.models

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class TouchDragObject(private var rootLayout: View) {

    var objet : Objet? = null


    fun onTouch(newObjet: Objet?, event: MotionEvent): Boolean {
        if (this.objet == null) this.objet = newObjet
        if(objet != null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    objet!!.setPositions(event.getX(), event.getY())
                }
                MotionEvent.ACTION_UP -> {
                    objet = null
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    objet!!.setPositions(event.getX(), event.getY())
                }
            }
            rootLayout.invalidate()
        }

        return true
    }

}