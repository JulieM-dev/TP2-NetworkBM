package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.updateBounds
import com.example.networkbm.R
import kotlin.math.sqrt

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

    /**
     * Ecris les connexions et objets
     */
    override fun draw(canvas: Canvas) {
        this.canvas = canvas

        reseau.connexions.forEach {
            it.draw(canvas)
        }

        reseau.objets.forEach {
            it.draw(canvas, this.context)
        }
    }

    /**
     * Set l'alpha
     */
    override fun setAlpha(newAlpha: Int) {
         greyAlpha = newAlpha
    }

    /**
     * Récupère l'alpha
     */
    override fun getAlpha(): Int {
        return greyAlpha
    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

    /**
     * Récupère l'opacité
     */
    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

}