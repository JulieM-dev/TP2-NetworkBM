package com.example.networkbm

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AjoutObjetForm (context: Context) : AppCompatActivity() {

    val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(context)
    lateinit var alertDialog : AlertDialog
    val editTextNom : EditText = findViewById(R.id.editTextNom)
    val spinnerCouleur : Spinner  = findViewById(R.id.spinnerCouleur)
    val buttonValider : Button = findViewById(R.id.buttonValider)
    val buttonAnnuler : Button = findViewById(R.id.buttonAnnuler)

    fun afficherFormulaire()
    {
        val formulaire : View = layoutInflater.inflate(R.layout.ajout_objet_form, null)


    }


}