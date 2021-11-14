package com.example.networkbm.models

class Graph() {
    var objets = ArrayList<Objet>()
    var connexions = ArrayList<Connexion>()
    var imgAppart = String()

    /**
     * Récupère un objet aux positions
     */
    fun getObjet(x : Float, y : Float) : Objet?
    {
        objets.forEach {
            if (it.contains(x,y)) return it
        }
        return null
    }

    /**
     * Récupère une connexion aux positions
     */
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

    /**
     * Vérifie s'il existe une connexion entre 2 objets
     */
    fun existeConnexion(objet1: Objet, objet2: Objet): Boolean {
        var ci = 0
        this.connexions.forEach{
            if( (it.getObjet1() == objet1 && it.getObjet2() == objet2) || (it.getObjet1() == objet2 && it.getObjet2() == objet1) ){
                ci++
            }
        }
        return ci > 1
    }

    /**
     * Vérifie si l'objet existe (pour un test de connexion)
     */
    fun existeObjet(objet: Objet) : Boolean
    {
        return objets.contains(objet)
    }

}