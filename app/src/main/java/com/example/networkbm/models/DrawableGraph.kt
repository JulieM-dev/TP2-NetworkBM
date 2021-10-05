package com.example.networkbm.models

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph : Drawable() {

    var reseau : Graph = Graph()
    lateinit var canvas: Canvas
    var paint = Paint()


    override fun draw(canvas: Canvas) {
        this.canvas = canvas

        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F

        reseau.connexions.forEach {
            canvas.drawLine(it.getObjet1().centerX(), it.getObjet1().centerY(), it.getObjet2()!!.centerX(), it.getObjet2()!!.centerY(), paint)
        }

        reseau.objets.forEach {
            paint.setColor(Color.parseColor(it.couleur))
            canvas.drawRoundRect(it,20F,20F, paint)
        }

    }

    override fun setAlpha(p0: Int) {

    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

}