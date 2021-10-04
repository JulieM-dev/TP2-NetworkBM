package com.example.networkbm.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class Connexion (objet1 : Objet, reseau: Reseau, context: Context) : View(context) {
    private val reseau = reseau
    private var objet1 = objet1
    private var objet2 : Objet? = null
    private var paint = Paint()

    fun getObjet1() : Objet
    {
        return objet1
    }

    fun getObjet2() : Objet?
    {
        return objet2
    }

    fun setObjet1(newObjet1: Objet)
    {
        this.objet1.connexions.remove(this)
        newObjet1.connexions.add(this)
        this.objet1 = newObjet1
    }

    fun setObjet2(newObjet2 : Objet)
    {
        this.objet2?.connexions?.remove(this)
        newObjet2.connexions.add(this)
        this.objet2 = newObjet2
    }

    fun remove()
    {
        objet1.connexions.remove(this)
        objet2?.connexions?.remove(this)
        reseau.removeConnexion(this)
    }

    override fun onDraw(canvas: Canvas) {
        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F
        if(objet2 != null)
        {
            canvas.drawLine(objet1.x,objet1.y, objet2!!.x, objet2!!.y, paint)
        }

    }
}