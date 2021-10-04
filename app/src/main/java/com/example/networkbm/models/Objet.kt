package com.example.networkbm.models

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.RelativeLayout

class Objet(context: Context, nom: String, couleur: String, savePosX: Int?, savePosY: Int?) : View(context) {

    private var bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    private lateinit var canvas :Canvas
    private var shapeDrawable = ShapeDrawable(RectShape())
    var nom : String = nom
    var couleur : String = couleur
    lateinit var touchDragObject : TouchDragObject
    var connexions = ArrayList<Connexion>()
    private var posX = savePosX
    private var posY = savePosY


    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
    }

    fun editRect(contPrinc: RelativeLayout){
        // rectangle positions
        val left = 200
        val top = 0
        val right = 800
        val bottom = 650
        // draw rectangle shape to canvas
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.paint.color = Color.parseColor("#009944")
        shapeDrawable.draw(canvas)
        var paint = Paint()
        paint.textSize = 183F

        paint.color = Color.BLACK;
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(nom, 500F, 850F, paint);

        var paint1 = Paint()
        paint1.textSize = 183F

        paint1.color = Color.BLACK;
        paint1.textAlign = Paint.Align.CENTER
        canvas.drawText(nom, 500F, 1000F, paint1);

        // set bitmap as background to ImageView
        this.background = BitmapDrawable(getResources(), bitmap)

    }

    fun createRect(contPrinc: RelativeLayout) {
        canvas = Canvas(bitmap)
        editRect(contPrinc)
        System.out.println("-----" + posX)
        if(posX == null && posY == null){
            this.translationX = (contPrinc.width / 2).toFloat()
            this.translationY = (contPrinc.height / 2).toFloat()
        } else {
            this.translationX = posX!!.toFloat()
            this.translationY = posY!!.toFloat()
        }
    }


    fun clear()
    {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

    fun changeColor(color: String){
        this.shapeDrawable.paint.color = Color.parseColor(color)
        shapeDrawable.draw(canvas)
    }

    fun addTouchDragObject(touchDragObject: TouchDragObject)
    {
        this.touchDragObject = touchDragObject
        setDragable(true)
    }

    fun setDragable(dragable: Boolean)
    {
        if(dragable)
        {
            this.setOnTouchListener(touchDragObject)
        }
        else{
            this.setOnTouchListener(null)
        }
    }

    fun isDragged()
    {
        connexions.forEach{
            it.moveConnexion()
        }
    }


}