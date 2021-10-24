package com.example.networkbm.models

import android.content.Context
import android.view.View
import java.io.ObjectOutputStream

class Reseau {

    var objets : ArrayList<Objet> = ArrayList<Objet>()
    private var connexions : ArrayList<Connexion> = ArrayList<Connexion>()

    fun addObjet(objet: Objet) {
        objets.add(objet)
    }

    fun removeObjet(objet: Objet){
        objets.remove(objet)
    }

    fun addConnexion(connexion: Connexion)
    {
        // TODO: 29/09/2021
    }

    fun removeConnexion(connexion: Connexion) {
        connexions.remove(connexion)
    }

}