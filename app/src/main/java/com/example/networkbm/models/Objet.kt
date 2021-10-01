package com.example.networkbm.models

import android.view.View

class Objet (nom: String, rect: Rectangle){
    var rect : Rectangle = rect
    var nom : String = nom


    fun getView() : View
    {
        return rect
    }
}