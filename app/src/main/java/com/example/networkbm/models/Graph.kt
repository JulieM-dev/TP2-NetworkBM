package com.example.networkbm.models

class Graph() {
    var objets = ArrayList<Objet>()
    var connexions = ArrayList<Connexion>()
    var imgAppart = String()

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

    fun existeConnexion(objet1: Objet, objet2: Objet): Boolean {
        var ci = 0
        this.connexions.forEach{
            if( (it.getObjet1() == objet1 && it.getObjet2() == objet2) || (it.getObjet1() == objet2 && it.getObjet2() == objet1) ){
                ci++
            }
        }
        return ci > 1
    }

    fun existeObjet(objet: Objet) : Boolean
    {
        return objets.contains(objet)
    }

    fun getJsonReadyInstance() : Graph
    {
        var graph = Graph();
        System.out.println("------------------------------------Avant to list")
        graph.objets = this.objets.toList() as ArrayList<Objet>
        System.out.println("------------------------------------Après to list")
        var objetsARemove = ArrayList<Objet>()
        graph.objets.forEach {
            System.out.println("------------------------------------It for each objets " + it.id)
            if(it.connexions.size >= 1)
            {
                it.connexions.forEach{
                    System.out.println("------------------------------------It for each connexions " + it.id)
                    if(!graph.existeConnexion(it.getObjet1(), it.getObjet2()!!))
                    {
                        System.out.println("------------------------------------connexion n'existe pas")
                        graph.connexions.add(it)
                        System.out.println("connexions size : " + graph.connexions.size)
                        it.reseau = null
                    }
                }
                System.out.println("------------------------------------Avant clear")
                it.connexions.clear()
                objetsARemove.add(it)
                System.out.println("------------------------------------Après clear")
            }

        }
        graph.objets.removeAll(objetsARemove)
        System.out.println("------------------------------------Return")
        System.out.println("connexions size : " + graph.connexions.size)
        return graph
    }

    fun restoreFromJsonReadyInstance(graph: Graph)
    {
        System.out.println("------------------------------------Debut restore")
        this.objets = graph.objets
        this.connexions = graph.connexions
        this.connexions.forEach {
            System.out.println("------------------------------------It for each connexions")
            it.getObjet1().connexions.add(it)
            if(!existeObjet(it.getObjet1()))
            {
                objets.add(it.getObjet1())
            }
            if(it.getObjet2() != null)
            {
                it.getObjet2()?.connexions!!.add(it)
                if(!existeObjet(it.getObjet2()!!))
                {
                    objets.add(it.getObjet2()!!)
                }
            }
            it.reseau = this

        }

        System.out.println("------------------------------------Fin restore")
    }

}