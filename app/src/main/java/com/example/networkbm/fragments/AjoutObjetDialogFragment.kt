package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.children
import com.example.networkbm.DeptListener
import com.example.networkbm.R
import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Graph
import com.example.networkbm.models.Objet
import java.lang.ClassCastException

class AjoutObjetDialogFragment() : AppCompatDialogFragment() {
    lateinit var listener: DeptListener
    lateinit var dialogBuilder : AlertDialog.Builder
    lateinit var alertDialog : AlertDialog
    lateinit var editTextNom : EditText
    lateinit var buttonValider : Button
    lateinit var buttonAnnuler : Button
    lateinit var buttonSupprimer : Button
    var objet : Objet? = null
    var reseau : Graph? = null
    var listCouleurs = arrayOf("#2d3436","#e74c3c","#2ecc71","#3498db","#e67e22","#00cec9","#9b59b6")
    var selectedColor : String? = null

    constructor(objet: Objet, reseau: Graph) : this() {
        this.objet = objet
        this.reseau = reseau
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)
        this.selectedColor = this.listCouleurs.get(0)

        val formulaire = activity?.layoutInflater?.inflate(R.layout.ajout_objet_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            editTextNom = formulaire.findViewById(R.id.editTextNom)
            if(objet != null)
            {
                editTextNom.setText(objet!!.nom)
                this.selectedColor = objet!!.couleur
                this.clickCouleur(objet!!.couleur!!, formulaire)
            }
            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)
            buttonSupprimer = formulaire.findViewById(R.id.buttonSupprimer)

            buttonValider.setOnClickListener()
            {
                val nom = editTextNom.text.toString()
                val depts = ArrayList<String>()
                depts.add(nom)
                depts.add(this.selectedColor!!)
                listener.onDeptSelected(depts)
                alertDialog.dismiss()
            }
            buttonAnnuler.setOnClickListener()
            {
                alertDialog.dismiss()
            }
            if(reseau != null)
            {
                buttonSupprimer.visibility = View.VISIBLE
                buttonSupprimer.setOnClickListener()
                {
                    var depts = ArrayList<String>()
                    var i = 0
                    val si = reseau!!.connexions.size
                    val con = this.reseau!!.connexions.clone() as ArrayList<Connexion>
                    while(i < si){
                        val it = con.get(i)
                        if(it.getObjet1() == objet || it.getObjet2() == objet){
                            reseau!!.connexions.remove(it)
                        }
                        i++
                    }
                    reseau!!.objets.remove(objet)
                    Toast.makeText(this.context, getString(R.string.objectDeleted), Toast.LENGTH_SHORT).show()
                    listener.onDeptSelected(depts)
                    alertDialog.dismiss()
                }
            }

            val layout = formulaire.findViewById<LinearLayout>(R.id.listCouleurs)
            var butNoir = formulaire.findViewById<Button>(R.id.butCol1)
            listCouleurs.forEach {
                val newBut = Button(buttonValider.context)
                newBut.layoutParams = butNoir.layoutParams
                newBut.setBackgroundColor(Color.parseColor(it))
                newBut.setTextColor(resources.getColor(R.color.white))
                val str = it.toString()
                newBut.setOnClickListener()
                {
                    this.clickCouleur(str, formulaire)
                }
                layout.addView(newBut)
            }
            layout.removeView(butNoir)
        }
        return alertDialog
    }

    fun clickCouleur(color: String, formulaire: View){
        this.selectedColor = color
        val layout = formulaire.findViewById<LinearLayout>(R.id.listCouleurs)
        layout.children.forEach {
            val but = it as Button
            val butCol = but.background as ColorDrawable
            but.text = ""
            if(butCol.color == Color.parseColor(color))
                but.text = "V"
            else
                but.text = ""
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            listener = context as DeptListener
        }
        catch (e: ClassCastException){
            throw ClassCastException(context.toString() + "doit implementer DeptListener")
        }

    }

}