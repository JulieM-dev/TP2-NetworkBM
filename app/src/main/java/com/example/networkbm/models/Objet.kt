package com.example.networkbm.models

import android.content.Context
import android.graphics.*

class Objet(var nom: String, x: Float, y: Float) : RectF(x-45, y-42, x+45, y+42) {

    var id = this.hashCode()
    var couleur : String? = null
    var icone : String? = null
    var connexions = ArrayList<Connexion>()
    val paint = Paint()

    constructor(nom: String, x: Float, y:Float, col: String?, ico: String?) : this(nom, x, y) {
        this.couleur = col
        this.icone = ico
    }

    init {
        paint.style = Paint.Style.FILL
        paint.setColor(Color.BLACK);
        paint.strokeWidth = 7F
        paint.textSize = 30F
        paint.textAlign = Paint.Align.CENTER
    }

    /**
     * Set les positions
     */
    fun setPositions(x : Float, y : Float)
    {
        this.left = x-45
        this.top = y-42
        this.right = x+45
        this.bottom = y+42
    }

    /**
     * Récupère les ID de connexions
     */
    fun getConnexionsId() : ArrayList<Int>
    {
        val listId = ArrayList<Int>()
        connexions.forEach {
            listId.add(it.id)
        }
        return listId
    }

    /**
     * Ecris l'objet sur le graphe
     */
    fun draw(canvas : Canvas, context: Context){


        if(this.couleur != null)
            paint.setColor(Color.parseColor(this.couleur))
        canvas.drawRoundRect(this,20F,20F, paint)
        canvas.drawText(this.nom, this.centerX(), this.centerY()+65, paint)
        if(this.icone != null){
            val path = context.resources.getIdentifier(this.icone, "drawable", context.packageName)
            var icon2 = BitmapFactory.decodeResource(context.resources, path)
            if(icon2 != null) {
                icon2 = Bitmap.createScaledBitmap(icon2, 80, 80, false)
                canvas.drawBitmap(icon2, this.centerX() - 40, this.centerY() - 40, null)
            }
        }
    }

    override fun toString(): String {
        return if(nom.isNotEmpty())
            nom
        else
            "Objet " + (this.left.toInt().toString() + "." + this.top.toInt())
    }

}