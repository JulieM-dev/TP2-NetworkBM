package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.networkbm.DeptListener
import com.example.networkbm.R
import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Graph
import java.lang.ClassCastException

class EditConnexionDialogFragment() : AppCompatDialogFragment() {
    lateinit var listener: DeptListener
    lateinit var dialogBuilder : AlertDialog.Builder
    lateinit var alertDialog : AlertDialog
    lateinit var editTextNom : EditText
    lateinit var spinnerCouleur : Spinner
    lateinit var buttonValider : Button
    lateinit var buttonAnnuler : Button
    lateinit var buttonSupprimer : Button
    var connexion : Connexion? = null
    var reseau : Graph? = null

    constructor(connexion: Connexion, reseau: Graph) : this() {
        this.connexion = this.connexion
        this.reseau = reseau
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)

        var formulaire = activity?.layoutInflater?.inflate(R.layout.edit_connexion_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            editTextNom = formulaire.findViewById(R.id.editTextNom)
            if(connexion != null)
            {

            }
            spinnerCouleur = formulaire.findViewById(R.id.spinnerCouleur)
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
            buttonSupprimer.setOnClickListener()
            {
                reseau!!.connexions.remove(connexion)
                alertDialog.dismiss()
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