package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.networkbm.R

class ChangePlanFragment : AppCompatDialogFragment() {
    lateinit var dialogBuilder : AlertDialog.Builder
    lateinit var alertDialog : AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)
        val formulaire = activity?.layoutInflater?.inflate(R.layout.edit_connexion_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        return alertDialog
    }
}