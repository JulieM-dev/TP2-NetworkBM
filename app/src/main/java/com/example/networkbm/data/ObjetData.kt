package com.example.networkbm.data

import com.example.networkbm.models.Objet

/**
 * Classe de donnée pour les Objets
 */
class ObjetData (obj: Objet){
    var id = obj.id
    var nom = obj.nom
    var x = obj.centerX()
    var y = obj.centerY()
    var couleur = obj.couleur.toString()
    var icone = obj.icone.toString()
    var connexions = obj.getConnexionsId()
}