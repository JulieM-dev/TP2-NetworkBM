package com.example.networkbm.models

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class TouchDragObject(private var rootLayout: View, reseau: Graph) {
    var reseau : Graph = reseau

    var objet : Objet? = null
    var connexion : Connexion? = null


    fun onTouch(newObjet: Objet?, event: MotionEvent): Boolean {
        if (this.objet == null) this.objet = newObjet
        if(objet != null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    objet!!.setPositions(event.getX(), event.getY())
                }
                MotionEvent.ACTION_UP -> {
                    objet = null
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    objet!!.setPositions(event.getX(), event.getY())
                }
            }
            rootLayout.invalidate()
        }

        return true
    }

    fun dragLine(newConnexion: Connexion?, event: MotionEvent): Boolean {
        if (this.connexion == null) this.connexion = newConnexion
        if(connexion != null)
        {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    connexion!!.setPositions(event.getX(), event.getY())
                }
                MotionEvent.ACTION_UP -> {
                    var obj = reseau.getObjet(connexion!!.getCords().get(2), connexion!!.getCords().get(3))
                    if (obj != null && obj != connexion!!.getObjet1() )
                    {
                        connexion!!.setObjet2(obj)
                    }
                    connexion = null
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                }
                MotionEvent.ACTION_POINTER_UP -> {
                }
                MotionEvent.ACTION_MOVE -> {
                    connexion!!.setPositions(event.getX(), event.getY())
                    System.out.println(connexion!!.getCords().get(2).toString() + "  Y : " + connexion!!.getCords().get(3))
                }
            }
            rootLayout.invalidate()
        }

        return true
    }

}