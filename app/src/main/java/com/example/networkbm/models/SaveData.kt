package com.example.networkbm.models

import android.content.Context
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class SaveData {

    var context: Context? = null
    var bytes = ByteArray(0)

    constructor(context: Context){
        this.context = context
    }

    fun getData(){
        try {
            val open = context!!.openFileInput("graph")
            var size = open.available()
            var bytes = ByteArray(size)
            size = open.read(bytes)
        } catch(e: IOException) {
            e.printStackTrace()
            Log.e("MyActivity", "erreur de lecture de graph")
        }
    }

    fun storeData(reseau: Reseau){
        try{
            val outputStream = context!!.openFileOutput("graph", Context.MODE_PRIVATE)
            outputStream.write(bytes)
            outputStream.flush()
            outputStream.close()
        } catch(e: IOException) {
            e.printStackTrace()
            Log.e("MyActivity", "erreur d'écriture de graph")
        }
    }
}