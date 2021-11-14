package com.example.networkbm.data

import com.example.networkbm.models.Connexion

/**
 * Classe de donnée pour les connexions
 */
class ConnexionData (conx: Connexion) {
    var id = conx.id
    var objet1 = conx.getObjet1().id
    var objet2 = conx.getObjet2()?.id
    var nom = conx.getNom()
    var couleur = conx.couleur
    var epaisseur = conx.epaisseur
    var courbure = conx.courbure
}