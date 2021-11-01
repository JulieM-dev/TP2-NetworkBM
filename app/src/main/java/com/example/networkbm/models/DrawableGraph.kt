package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.example.networkbm.R

class DrawableGraph(var context: Context) : Drawable() {

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

            if(it.couleur != null){
                paintStroke.setColor(Color.parseColor(it.couleur))
                paintStroke.strokeWidth = it.epaisseur
                paint.setColor(Color.parseColor(it.couleur))
            }

            it.rewind()
            it.moveTo(cords.get(0), cords.get(1))
            it.lineTo(cords.get(2), cords.get(3))
            canvas.drawPath(it, paintStroke)

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
            if(it.icone != null){
                val path = this.context.resources.getIdentifier(it.icone, "drawable", this.context.packageName)
                var icon2 = BitmapFactory.decodeResource(this.context.resources, path);
                icon2 = Bitmap.createScaledBitmap(icon2, 80, 80, false);
                canvas.drawBitmap(icon2, it.centerX()-40, it.centerY()-40, null)
            }
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