package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.view.View

class Connexion (objet1 : Objet, reseau: Graph, context: Context) : Path() {
    private val reseau = reseau
    private var objet1 = objet1
    private var objet2 : Objet? = null

    init {
        actualisePath()
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
        actualisePath()
    }

    fun setObjet2(newObjet2 : Objet)
    {
        this.objet2?.connexions?.remove(this)
        newObjet2.connexions.add(this)
        this.objet2 = newObjet2
        actualisePath()
    }

    fun remove()
    {
        this.remove()
        objet1.connexions.remove(this)
        objet2?.connexions?.remove(this)
        reseau.connexions.remove(this)
    }

}