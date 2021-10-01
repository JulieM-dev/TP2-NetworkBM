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
import java.lang.ClassCastException

class AjoutObjetDialogFragment : AppCompatDialogFragment() {
    lateinit var listener: DeptListener
    lateinit var dialogBuilder : AlertDialog.Builder
    lateinit var alertDialog : AlertDialog
    lateinit var editTextNom : EditText
    lateinit var spinnerCouleur : Spinner
    lateinit var buttonValider : Button
    lateinit var buttonAnnuler : Button


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)

        var formulaire = activity?.layoutInflater?.inflate(R.layout.ajout_objet_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle("Ajout objet")

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            editTextNom = formulaire.findViewById(R.id.editTextNom)
            spinnerCouleur = formulaire.findViewById(R.id.spinnerCouleur)
            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)



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
        }


        return alertDialog

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            listener = context as DeptListener
        }
        catch (e: ClassCastException){
            throw ClassCastException(context.toString() +
            "doit implementer DeptListener")
        }


    }

}