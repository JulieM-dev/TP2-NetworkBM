package com.example.networkbm.models

import android.view.MotionEvent
import android.widget.ImageView
import com.example.networkbm.views.WScrollView

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
        if(connexion != null)
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

}