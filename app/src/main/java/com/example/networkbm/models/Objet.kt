package com.example.networkbm.models

import android.view.View
import android.widget.TextView

class Objet (nom: String, couleur: String, rect: Rectangle){
    var rect : Rectangle = rect
    var nom : String = nom
    var couleur : String = couleur

    init {
        rect.changeColor(couleur)
    }



    fun getView() : View
    {
        return rect
    }
}