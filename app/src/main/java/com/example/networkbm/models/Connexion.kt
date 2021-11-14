package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import com.google.gson.annotations.Expose

class Connexion (objet1 : Objet, reseau: Graph?) : Path() {
    var id = this.hashCode()
    var reseau = reseau
    private var objet1 = objet1
    private var objet2 : Objet? = null
    private var nom : String? = null
    var pointerX = 50F
    var pointerY = 0F
    var couleur : String? = null
    var epaisseur = 10F
    var paint = Paint()
    var paintText = Paint()
    var courbure = 1000

    init {
        paint.style = Paint.Style.STROKE
        paint.setColor(Color.BLACK);

        paintText.style = Paint.Style.FILL
        paintText.setColor(Color.BLACK);
        paintText.strokeWidth = 7F
        updatePaint()

    }



    constructor(objet1 : Objet, objet2 : Objet?, col: String?, epaisseur: Float) : this(objet1, null) {
        this.couleur = col
        this.epaisseur = epaisseur
        this.objet2 = objet2
    }

    fun setPositions(x : Float, y : Float)
    {
        pointerX = x
        pointerY = y
    }

    fun draw(canvas : Canvas)
    {
        rewind()
        var radius = 1000

        var cords = getCords()
        // cords[0] = objet1.x
        // cords[1] = objet1.y
        // si objet2 != null:
        //      cords[2] = objet2.x
        //      cords[3] = objet2.y
        // sinon :
        //      cords[2] = position X du doigt
        //      cords[3] = position Y du doigt

        moveTo(cords[0], cords[1])
        var centerCords = listOf((cords[0] + cords[2])/2, (cords[1] + cords[3])/2)

        var m1 = (cords[3] - cords[1]) / (cords[2] - cords[0])
        var m2 = -(1/m1)
        var h = -(m2 * centerCords[0] - centerCords[1])
        var mediatriceY = m2 * 3 + h
        var quadX = centerCords[0]
        var quadY = centerCords[1]
        if(cords[1] > cords[3])
        {
            quadX = (centerCords[0] + (radius / Math.sqrt(Math.pow( m2.toDouble(), 2.0)+1))).toFloat()
            quadY = (centerCords.get(1) + (m2 * radius / Math.sqrt(Math.pow( m2.toDouble(), 2.0)+1))).toFloat()
        }
        else if (cords[1] < cords[3])
        {
            quadX = (centerCords[0] - (radius / Math.sqrt(Math.pow(m2.toDouble(), 2.0)+1))).toFloat()
            quadY = (centerCords[1] - (m2 * radius / Math.sqrt(Math.pow( m2.toDouble(), 2.0)+1))).toFloat()
        }
        else if(cords[1] == cords[3])
        {
            quadX = (centerCords[0])
            quadY = (centerCords[1] + radius)
        }

        quadTo(quadX, quadY , cords[2], cords[3])
        canvas.drawPath(this, paint)

        cords = getCenter()
        if (nom != null)
        {
            canvas.drawText(nom!!, getCenter()[0], getCenter()[1] + 50, paint)
        }

    }

    fun actualisePath()
    {
        this.rewind()
        this.setLastPoint(objet1.centerX(), objet1.centerY())
        if(objet2 != null)
        {
            this.lineTo(objet2!!.centerX(), objet2!!.centerY())
        }
        else
        {
            this.lineTo(50f,50f)
        }
    }

    fun getCords() : List<Float>
    {
        if(objet2 == null)
        {
            return listOf(objet1.centerX(), objet1.centerY(), pointerX, pointerY)
        }
        return listOf(objet1.centerX(), objet1.centerY(), objet2!!.centerX(), objet2!!.centerY())
    }

    fun getCenter(): List<Float> {
        var cords = getCords()
        var centerX = (cords.get(0) + cords.get(2)) / 2
        var centerY = (cords.get(1) + cords.get(3)) / 2
        return listOf(centerX, centerY)
    }

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
        if(this.objet1.connexions.contains(this))
        {
            this.objet1.connexions.remove(this)
        }
        newObjet1.connexions.add(this)
        this.objet1 = newObjet1
        actualisePath()
    }

    fun setObjet2(newObjet2 : Objet)
    {
        if(this.objet2 != null && this.objet2!!.connexions.contains(this))
        {
            this.objet2!!.connexions.remove(this)
        }
        newObjet2.connexions.add(this)
        this.objet2 = newObjet2
        actualisePath()
    }

    fun remove()
    {
        objet1.connexions.remove(this)
        objet2?.connexions?.remove(this)
        reseau!!.connexions.remove(this)
    }

    fun setNom(nom: String){
        this.nom = nom
    }

    fun getNom() : String? {
        return this.nom
    }

    fun setColor(color: String){
        this.couleur = color
    }

    fun updatePaint()
    {
        if(couleur != null){
            paint.setColor(Color.parseColor(couleur))
        }
        paint.strokeWidth = epaisseur

        if(nom != null){
            paintText.textSize = 30F
            paintText.textAlign = Paint.Align.CENTER
        }
    }


}