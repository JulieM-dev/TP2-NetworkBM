package com.example.networkbm.models

enum class Mode(private val type: Int) {
    AJOUT_OBJET(0), AJOUT_CONNEXION(1), MODIFICATION(2);

    fun getMode(): Int {
        return type
    }

    fun getLibelle() : String? {
        var s : String? = null
        when(type){
            0 -> s = "AJOUT_OBJET"
            1 -> s = "AJOUT_CONNEXION"
            2 -> s = "MODIFICATION"
        }
        return s
    }
}