package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
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

    constructor(objet: Objet, reseau: Graph) : this() {
        this.objet = objet
        this.reseau = reseau
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)

        var formulaire = activity?.layoutInflater?.inflate(R.layout.ajout_objet_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            editTextNom = formulaire.findViewById(R.id.editTextNom)
            if(objet != null)
            {
                editTextNom.setText(objet!!.nom)
            }
            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)
            buttonSupprimer = formulaire.findViewById(R.id.buttonSupprimer)

            buttonValider.setOnClickListener()
            {
                var nom = editTextNom.text.toString()
                var depts = ArrayList<String>()
                depts.add(nom)
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

        }
        return alertDialog
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