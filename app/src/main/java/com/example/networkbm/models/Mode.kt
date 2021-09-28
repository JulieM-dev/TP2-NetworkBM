package com.example.networkbm.models

enum class Mode(private val type: Int, private val libelle: String) {
    AUCUN(0, "Aucun"),
    AJOUT_OBJET(1, "Ajoutez un objet"),
    AJOUT_CONNEXION(2, "Ajoutez une connexion"),
    MODIFICATION(3, "Modifiez un objet ou connexion");

    fun getMode(): Int {
        return type
    }

    fun getLibelle() : String {
        return libelle
    }
}