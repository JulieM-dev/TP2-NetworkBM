package com.example.networkbm.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class SaveReseau {

    private lateinit var listObjets : ArrayList<Objet>
    private lateinit var listConnexion: ArrayList<Connexion>

    fun read(context : Context, reseau: Graph): String? {
        try {
            //LISTE DES OBJETS
            var fis = context.openFileInput("objets")
            var isr = InputStreamReader(fis)
            var bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine()
            }
            val gson = Gson()
            val listObjets: List<Objet> = gson.fromJson(sb.toString(), Array<Objet>::class.java).toList()
            listObjets.forEach{
                reseau.objets.add(it)
            }

            //LISTE DES CONNEXIONS
            fis = context.openFileInput("connexions")
            isr = InputStreamReader(fis)
            bufferedReader = BufferedReader(isr)
            line = bufferedReader.readLine()
            sb.clear()
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine()
            }
            System.out.println(sb.toString())
            val listConnexions: List<Connexion> = gson.fromJson(sb.toString(), Array<Connexion>::class.java).toList()
            listConnexions.forEach{
                val connect = Connexion(reseau.objets.get(0), reseau)
                val conIt = it
                reseau.objets.forEach{
                    if(conIt.getObjet1().centerX() == it.centerX() &&
                        conIt.getObjet1().centerY() == it.centerY()){
                        it.connexions.add(connect)
                        connect.setObjet1(it)
                    } else if(conIt.getObjet2()!!.centerX() == it.centerX() &&
                        conIt.getObjet2()!!.centerY() == it.centerY()){
                        it.connexions.add(connect)
                        connect.setObjet2(it)
                    }
                }
                if(conIt.couleur != null){
                    connect.setColor(conIt.couleur!!)
                }
                connect.epaisseur = conIt.epaisseur
                reseau.connexions.add(connect)
            }

            return sb.toString();
        } catch (fileNotFound : FileNotFoundException) {
            return null;
        } catch (ioException : IOException) {
            return null;
        }
    }

    fun create(context: Context, reseau: Graph) : Boolean{
        try {
            //LISTE DES OBJETS
            this.listObjets = ArrayList<Objet>()
            reseau.objets.forEach{
                this.listObjets.add(Objet(it.nom, it.centerX(), it.centerY(), it.couleur))
            }
            var gson = Gson()
            var str = gson.toJson(this.listObjets)
            var fos = context.openFileOutput("objets",Context.MODE_PRIVATE);
            if (str != null) {
                fos.write(str.toByteArray());
            }
            fos.close();

            //LISTE DES CONNEXIONS
            this.listConnexion = ArrayList<Connexion>()
            reseau.connexions.forEach{
                val o1 = Objet(it.getObjet1().nom, it.getObjet1().centerX(), it.getObjet1().centerY())
                val o2 = Objet(it.getObjet2()!!.nom, it.getObjet2()!!.centerX(), it.getObjet2()!!.centerY())
                this.listConnexion.add(Connexion(o1, o2, it.couleur, it.epaisseur))
            }
            gson = Gson()
            str = gson.toJson(this.listConnexion)
            fos = context.openFileOutput("connexions",Context.MODE_PRIVATE);
            if (str != null) {
                fos.write(str.toByteArray());
            }
            fos.close();

            return true;
        } catch (fileNotFound : FileNotFoundException) {
            return false;
        } catch (ioException : IOException) {
            return false;
        }
    }
}