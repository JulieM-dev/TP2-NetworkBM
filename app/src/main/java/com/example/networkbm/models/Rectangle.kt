package com.example.networkbm.models

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

class Rectangle(context: Context) : View(context) {

    private var bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    private lateinit var canvas :Canvas
    private var shapeDrawable = ShapeDrawable(RectShape())


    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
    }

    fun createRect(contPrinc: RelativeLayout){
        canvas = Canvas(bitmap)
        // rectangle positions
        val left = 300
        val top = 300
        val right = 400
        val bottom = 400
        // draw rectangle shape to canvas
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.paint.color = Color.parseColor("#009944")
        shapeDrawable.draw(canvas)
        var paint = Paint()
        paint.textSize = 50F

        paint.color = Color.BLACK;
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Some Text", 350F, 450F, paint);

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