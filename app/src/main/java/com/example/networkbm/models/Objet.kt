package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import com.google.gson.Gson
import com.google.gson.annotations.Expose

class Objet(nom: String, x: Float, y: Float) : RectF(x-45, y-42, x+45, y+42) {

    var id = this.hashCode()
    var nom = nom
    var couleur : String? = null
    var icone : String? = null
    var connexions = ArrayList<Connexion>()

    constructor(nom: String, x: Float, y:Float, col: String?, ico: String?) : this(nom, x, y) {
        this.couleur = col
        this.icone = ico
    }

    fun setPositions(x : Float, y : Float)
    {
        this.left = x-45
        this.top = y-42
        this.right = x+45
        this.bottom = y+42
    }

    fun getConnexionsId() : ArrayList<Int>
    {
        var listId = ArrayList<Int>()
        connexions.forEach {
            listId.add(it.id)
        }
        return listId
    }

    override fun toString(): String {
        if(nom.length > 0)
            return nom
        else
            return "Objet " + (this.left.toInt().toString() + "." + this.top.toInt())
    }

}