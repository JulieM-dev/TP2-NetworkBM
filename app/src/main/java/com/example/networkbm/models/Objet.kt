package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.RelativeLayout

class Objet(context: Context, nom: String, x: Float, y: Float) : RectF(x-45, y-42, x+45, y+42) {

    var nom = nom
    var couleur : String = "#009944"
    var connexions = ArrayList<Connexion>()

    fun setPositions(x : Float, y : Float)
    {
        this.left = x-45
        this.top = y-42
        this.right = x+45
        this.bottom = y+42
    }

    override fun toString(): String {
        if(nom.length > 0)
            return nom
        else
            return "Objet " + (this.left.toInt().toString() + "." + this.top.toInt())
    }


}