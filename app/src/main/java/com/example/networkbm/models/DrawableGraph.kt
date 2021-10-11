package com.example.networkbm.models

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph : Drawable() {

    var reseau : Graph = Graph()
    lateinit var canvas: Canvas
    var paint = Paint()
    var greyAlpha = 255


    override fun draw(canvas: Canvas) {
        this.canvas = canvas

        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F

        reseau.connexions.forEach {
            var cords = it.getCords()
            if(it.couleur != null)
                paint.setColor(Color.parseColor(it.couleur))
            canvas.drawLine(cords.get(0), cords.get(1), cords.get(2), cords.get(3), paint)
            cords = it.getCenter()
            canvas.drawCircle(cords.get(0), cords.get(1), 20F, paint)
            if(it.getNom() != null){
                paint.textSize = 30F
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(it.getNom()!!, it.getCenter().get(0), it.getCenter().get(1) + 50, paint)
            }
        }


        reseau.objets.forEach {
            paint.setColor(Color.parseColor(it.couleur))
            canvas.drawRoundRect(it,20F,20F, paint)
            if(it.couleur != null)
                paint.setColor(Color.parseColor(it.couleur))
            paint.textSize = 30F
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(it.nom, it.centerX(), it.centerY()+65, paint)
        }

    }

    override fun setAlpha(newAlpha: Int) {
         greyAlpha = newAlpha
    }

    override fun getAlpha(): Int {
        return greyAlpha
    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

}