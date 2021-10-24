package com.example.networkbm.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

class Graph() : Parcelable {
    var objets = ArrayList<Objet>()
    var connexions = ArrayList<Connexion>()

    constructor(parcel: Parcel) : this() {

    }

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
            val cords = it.getCenter()
            if(x < cords.get(0) + 25  && x > cords.get(0) - 25 &&
                y < cords.get(1) + 25  && y > cords.get(1) - 25){
                return it
            }
        }
        return null
    }

    companion object CREATOR : Parcelable.Creator<Graph> {
        override fun createFromParcel(parcel: Parcel): Graph {
            return Graph(parcel)
        }

        override fun newArray(size: Int): Array<Graph?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
}