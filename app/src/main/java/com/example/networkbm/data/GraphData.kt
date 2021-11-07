package com.example.networkbm.data

import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Objet

class GraphData {
    var objets = ArrayList<ObjetData>()
    var connexions = ArrayList<ConnexionData>()

    fun getObjet(id :Int) : ObjetData?
    {
        objets.forEach {
            if (it.contains(x,y)) return it
        }
        return null
    }

    fun getConnexion(x : Float, y: Float) : Connexion?
    {
        connexions.forEach {
            val cords = it.getCenter()
            if(x < cords.get(0) + 25  && x > cords.get(0) - 25 &&
                y < cords.get(1) + 25  && y > cords.get(1) - 25){
                return it
            }
        }
        return null
    }

}