package com.example.networkbm.models

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph : Drawable() {

    var reseau : Graph = Graph()
    lateinit var canvas: Canvas
    var paint = Paint()
    var paintStroke = Paint()
    var greyAlpha = 255

    init {
        paint.style = Paint.Style.FILL
        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F

        paintStroke.style = Paint.Style.STROKE
        paintStroke.setColor(Color.BLACK);

        paintStroke.strokeWidth = 7F
    }

    override fun draw(canvas: Canvas) {
        this.canvas = canvas

        paint.style = Paint.Style.FILL
        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F


        reseau.connexions.forEach {
            var cords = it.getCords()
            it.rewind()
            it.moveTo(cords.get(0), cords.get(1))
            it.lineTo(cords.get(2), cords.get(3))
            canvas.drawPath(it, paintStroke)
            cords = it.getCenter()
            canvas.drawCircle(cords.get(0), cords.get(1), 20F, paint)
        }


        reseau.objets.forEach {
            paint.setColor(Color.parseColor(it.couleur))
            canvas.drawRoundRect(it,20F,20F, paint)
            paint.setColor(Color.parseColor("#000000"))
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