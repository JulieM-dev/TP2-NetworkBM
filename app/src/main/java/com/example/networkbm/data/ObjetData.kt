package com.example.networkbm.data

import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Objet

class ObjetData (obj: Objet, id: Int){
    var id : Int = id
    var nom : String = obj.nom
    var x: Float = obj.centerX()
    var y: Float = obj.centerY()
    var couleur : String = obj.couleur.toString()
    var icone : String = obj.icone.toString()
    var connexions = ArrayList<Int>()
}