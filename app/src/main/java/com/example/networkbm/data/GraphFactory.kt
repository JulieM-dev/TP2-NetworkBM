package com.example.networkbm.data

import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Graph
import com.example.networkbm.models.Objet

class GraphFactory {
    companion object Factory
    {
        fun getGraph(graphData: GraphData) : Graph
        {
            val graph = Graph()
            graphData.objets.forEach {
                val obj = Objet(it.nom, it.x, it.y)
                obj.id = it.id
                obj.couleur = it.couleur
                obj.icone = it.icone
                graph.objets.add(obj)
            }
            graphData.connexions.forEach {
                if(it.objet2 != null)
                {
                    val obj1 = getObjet(it.objet1, graph)
                    val obj2 = getObjet(it.objet2!!, graph)
                    val conx = Connexion(obj1!!, obj2, it.couleur, it.epaisseur)
                    conx.id = it.id
                    conx.setObjet1(obj1)
                    conx.setObjet2(obj2!!)
                    conx.setNom(it.nom!!)
                    conx.reseau = graph
                    graph.connexions.add(conx)
                }
            }
            return graph
        }

        fun getGraphData(graph: Graph) : GraphData
        {
            val graphData = GraphData()
            graph.connexions.forEach {
                graphData.connexions.add(ConnexionData(it))
            }
            graph.objets.forEach {
                graphData.objets.add(ObjetData(it))
            }
            graphData.imgAppart = graph.imgAppart
            return graphData
        }

        private fun getObjet(id :Int, graph: Graph) : Objet?
        {
            graph.objets.forEach {
                if (it.id == id) return it
            }
            return null
        }

    }

}