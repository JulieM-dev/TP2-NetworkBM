package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.networkbm.R

class AjoutObjetDialogFragment : AppCompatDialogFragment() {
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
                // TODO: 30/09/2021
            }

            buttonAnnuler.setOnClickListener()
            {
                alertDialog.dismiss()
            }
        }


        return alertDialog

    }
}