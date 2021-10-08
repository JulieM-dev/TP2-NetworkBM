package com.example.networkbm.models

class Graph {
    var objets = ArrayList<Objet>()
    var connexions = ArrayList<Connexion>()

    fun getObjet(x : Float, y : Float) : Objet?
    {
        objets.forEach {
            if (it.contains(x,y)) return it
        }
        return null
    }

    fun getConnexion(x : Float, y: Float) : Connexion?
    {
        connexions.forEach {
            var cords = it.getCenter()
            if(x < cords.get(0) + 50  && x > cords.get(0) - 50 &&
                y < cords.get(1) + 50  && x > cords.get(1) - 50){
                return it
            }
        }
        return null
    }
}