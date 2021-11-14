package com.example.networkbm.models

import android.content.Context
import com.example.networkbm.data.GraphData
import com.example.networkbm.data.GraphFactory
import com.google.gson.Gson
import java.io.*

class SaveReseau {

    // private lateinit var listObjets : ArrayList<Objet>
    // private lateinit var listConnexion: ArrayList<Connexion>

    fun read(context : Context, reseau: Graph): Boolean {
        try {
            val fis = context.openFileInput("graph")
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                sb.append(line)
                line = bufferedReader.readLine()
            }
            val gson = Gson()
            val graphData : GraphData = gson.fromJson(sb.toString(), GraphData::class.java)

            // Conversion de data vers objet concret
            val graph = GraphFactory.getGraph(graphData)

            reseau.objets = graph.objets
            reseau.connexions = graph.connexions
            reseau.imgAppart = graphData.imgAppart
            return true
        } catch (fileNotFound : FileNotFoundException) {
            return false
        } catch (ioException : IOException) {
            return false
        }
    }

    fun create(context: Context, reseau: Graph) : Boolean{
        try {
            // Conversion d'objet concret vers data pour éviter la récursivité
            val graphData = GraphFactory.getGraphData(reseau)
            val gson = Gson()
            val str = gson.toJson(graphData)


            val fos = context.openFileOutput("graph",Context.MODE_PRIVATE)
            if (str != null) {
                fos.write(str.toByteArray())
            }
            fos.close()

            return true
        } catch (fileNotFound : FileNotFoundException) {
            return false
        } catch (ioException : IOException) {
            return false
        }
    }
}