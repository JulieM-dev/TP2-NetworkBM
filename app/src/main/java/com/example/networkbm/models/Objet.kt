package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.RelativeLayout

class Objet(context: Context, nom: String, x: Float, y: Float) : RectF(x, y, x+100, y+100) {

    var nom = nom
    var couleur : String = "#009944"
    var connexions = ArrayList<Connexion>()



}