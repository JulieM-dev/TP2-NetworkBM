package com.example.networkbm.models

import android.view.MotionEvent
import android.widget.ImageView
import com.example.networkbm.views.WScrollView
import kotlin.math.pow
import kotlin.math.sqrt

class TouchDragObject(private var rootLayout: WScrollView, var reseau: Graph,
                      private var plan: ImageView
) {

    var objet : Objet? = null
    var connexion : Connexion? = null


    fun onTouch(newObjet: Objet?, event: MotionEvent, x : Float, y : Float): Boolean {
        var ret = false
        if (this.objet == null) this.objet = newObjet
        if(objet != null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    objet!!.setPositions(x, y)
                    ret = true
                }
                MotionEvent.ACTION_UP -> {
                    objet = null
                    ret = false
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    when {
                        event.y < 42 -> {
                            objet!!.setPositions(x, 42f)
                        }
                        y > plan.height - 70 -> {
                            objet!!.setPositions(x, plan.height.toFloat() - 70)
                        }
                        else -> {
                            objet!!.setPositions(x, y)
                        }
                    }
                    ret = true
                }
            }
            rootLayout.invalidate()
        }

        return ret
    }

    fun dragLine(newConnexion: Connexion?, event: MotionEvent, x: Float, y: Float): Boolean {
        var ret = false
        if (this.connexion == null) this.connexion = newConnexion
        if(connexion != null && connexion!!.getObjet2() == null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    connexion!!.setPositions(x,y)
                    ret = true
                }
                MotionEvent.ACTION_UP -> {
                    val obj = reseau.getObjet(
                        connexion!!.getCords()[2],
                        connexion!!.getCords()[3]
                    )
                    if (obj != null && obj != connexion!!.getObjet1() )
                    {
                        connexion!!.setObjet2(obj)
                    }
                    connexion = null
                    ret = false
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    connexion!!.setPositions(x,y)
                    ret = true
                }
            }
            rootLayout.invalidate()
        }
        return ret
    }

    fun dragCourbure(connexion: Connexion?, event: MotionEvent, x: Float, y: Float): Boolean
    {
        var ret = false
        if (this.connexion == null) this.connexion = connexion
        if(this.connexion != null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {

                    val distance = sqrt((x - this.connexion!!.getCenter()[0].toDouble()).pow(2.0) + Math.pow(y - this.connexion!!.getCenter()[1].toDouble(), 2.0))
                    this.connexion!!.courbure = distance.toInt()
                    val quadCords = this.connexion!!.getQuadCords()
                    if(!(quadCords[0] > x-60 && quadCords[0] < x+60) || !(quadCords[1] > y-60 && quadCords[1] < y+60))
                    {
                        this.connexion!!.courbure = (-(distance)).toInt()
                    }
                    ret = true
                }
                MotionEvent.ACTION_UP -> {
                    this.connexion = null
                    ret = false
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    val distance = sqrt((x - this.connexion!!.getCenter()[0].toDouble()).pow(2.0) + Math.pow(y - this.connexion!!.getCenter()[1].toDouble(), 2.0))
                    this.connexion!!.courbure = distance.toInt()
                    val quadCords = this.connexion!!.getQuadCords()
                    if(!(quadCords[0] > x-60 && quadCords[0] < x+60) || !(quadCords[1] > y-60 && quadCords[1] < y+60))
                    {
                        this.connexion!!.courbure = (-(distance)).toInt()
                    }
                    ret = true
                }
            }
            rootLayout.invalidate()
        }

        return ret
    }

}