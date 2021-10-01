package com.example.networkbm.models

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.RelativeLayout

class Rectangle(context: Context) : View(context) {

    private var bitmap = Bitmap.createBitmap(700, 700, Bitmap.Config.ARGB_8888)
    private var canvas = Canvas(bitmap)
    private var shapeDrawable = ShapeDrawable(RectShape())

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
    }

    fun createRect(contPrinc: RelativeLayout){
        // rectangle positions
        val left = 100
        val top = 100
        val right = 1000
        val bottom = 1000

        // draw rectangle shape to canvas
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.paint.color = Color.parseColor("#009944")
        shapeDrawable.draw(canvas)

        // set bitmap as background to ImageView
        this.background = BitmapDrawable(getResources(), bitmap)
        //test2
        this.translationX = (contPrinc.width / 2).toFloat()
        this.translationY = (contPrinc.height / 2).toFloat()
    }

    fun changeColor(color: String){
        this.shapeDrawable.paint.color = Color.parseColor(color)
        shapeDrawable.draw(canvas)
    }
}