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

class EditConnexionDialogFragment() : AppCompatDialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var listener: DeptListener
    private lateinit var dialogBuilder : AlertDialog.Builder
    private lateinit var alertDialog : AlertDialog
    private lateinit var buttonValider : Button
    private lateinit var buttonAnnuler : Button
    private lateinit var buttonSupprimer : Button
    private lateinit var editObjet1 : Spinner
    private lateinit var editObjet2 : Spinner
    private lateinit var nomConnexion: EditText
    lateinit var barEpaisseur: SeekBar
    lateinit var nbrEpaisseur: TextView
    private var listCouleurs = arrayOf("#2d3436","#e74c3c","#2ecc71","#3498db","#e67e22","#00cec9","#9b59b6")
    var connexion : Connexion? = null
    var reseau : Graph? = null
    private var obj1 : Objet? = null
    private var obj2: Objet? = null
    private var idT1 = 0
    private var idT2 = 0
    private var isEdition = false
    private var selectedColor : String? = null

    constructor(connexion: Connexion, reseau: Graph, isEdition: Boolean = false) : this() {
        this.connexion = connexion
        this.reseau = reseau
        this.obj1 = connexion.getObjet1()
        this.obj2 = connexion.getObjet2()
        this.isEdition = isEdition
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)
        this.selectedColor = this.listCouleurs[0]

        val formulaire = activity?.layoutInflater?.inflate(R.layout.edit_connexion_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            nomConnexion = formulaire.findViewById(R.id.editTextNomConnexion)
            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)
            buttonSupprimer = formulaire.findViewById(R.id.buttonSupprimer)
            editObjet1 = formulaire.findViewById(R.id.editObjet1)
            editObjet2 = formulaire.findViewById(R.id.editObjet2)
            barEpaisseur = formulaire.findViewById(R.id.barEpaisseur)
            nbrEpaisseur = formulaire.findViewById(R.id.nbrEpaisseur)

            //Affichage de la liste de couleurs
            val layout = formulaire.findViewById<LinearLayout>(R.id.listCouleurs)
            val butNoir = formulaire.findViewById<Button>(R.id.butCol1)
            listCouleurs.forEach {
                val newBut = Button(buttonValider.context)
                newBut.layoutParams = butNoir.layoutParams
                newBut.setBackgroundColor(Color.parseColor(it))
                newBut.setTextColor(resources.getColor(R.color.white))
                val str = it
                newBut.setOnClickListener()
                {
                    this.clickCouleur(str, formulaire)
                }
                layout.addView(newBut)
            }
            layout.removeView(butNoir)

            if(this.isEdition){
                buttonSupprimer.visibility = View.INVISIBLE
                this.clickCouleur(this.listCouleurs[0], formulaire)
            } else {
                this.selectedColor = connexion!!.couleur
                this.clickCouleur(connexion!!.couleur!!, formulaire)
            }

            this.setList()
            this.idT1 = editObjet1.id
            this.idT2 = editObjet2.id
            editObjet1.onItemSelectedListener = this
            editObjet2.onItemSelectedListener = this

            buttonValider.setOnClickListener()
            {
                this.valider()
            }
            buttonAnnuler.setOnClickListener()
            {
                if(this.isEdition){
                    this.sendSupprimer()
                }
                alertDialog.dismiss()
            }
            buttonSupprimer.setOnClickListener()
            {
                this.sendSupprimer()
            }

            barEpaisseur.progress = (connexion!!.epaisseur * 2.5).toInt()
            nbrEpaisseur.text = barEpaisseur.progress.toString()
            barEpaisseur.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    nbrEpaisseur.text = barEpaisseur.progress.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })
        }

        return alertDialog
    }

    private fun sendSupprimer(){
        val depts = ArrayList<String>()
        connexion!!.remove()
        //reseau!!.connexions.remove(connexion)
        Toast.makeText(this.context, getString(R.string.connectionDeleted), Toast.LENGTH_SHORT).show()
        listener.onDeptSelected(depts)
        alertDialog.dismiss()
    }

    private fun setList(){
        val ar1 = reseau!!.objets.clone() as ArrayList<Objet>
        ar1.remove(this.obj2)
        val aa1 = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, ar1)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editObjet1.adapter = aa1

        val ar2 = reseau!!.objets.clone() as ArrayList<Objet>
        ar2.remove(this.obj1)
        val aa2 = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, ar2)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editObjet2.adapter = aa2

        editObjet1.setSelection(ar1.indexOf(this.obj1))
        editObjet2.setSelection(ar2.indexOf(this.obj2))
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (this.idT1 == p0?.id) {
            val o1 = this.editObjet1.adapter.getItem(p2) as Objet
            if(o1 != this.obj1){
                this.obj1 = o1
                this.setList()
            }
        } else {
            val o2 = this.editObjet2.adapter.getItem(p2) as Objet
            if(o2 != this.obj2){
                this.obj2 = o2
                this.setList()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun valider(){
        val depts = ArrayList<String>()
        Toast.makeText(this.context, getString(R.string.connectionModified), Toast.LENGTH_SHORT).show()

        val o1 = this.reseau!!.getObjet(this.obj1!!.centerX(), this.obj1!!.centerY())
        val o2 = this.reseau!!.getObjet(this.obj2!!.centerX(), this.obj2!!.centerY())

        connexion!!.setObjet1(o1!!)
        connexion!!.setObjet2(o2!!)
        connexion!!.epaisseur = (barEpaisseur.progress / 2.5).toFloat()
        connexion!!.setNom(this.nomConnexion.text.toString())
        connexion!!.setColor(this.selectedColor!!)
        listener.onDeptSelected(depts)
        alertDialog.dismiss()
    }

    private fun clickCouleur(color: String, formulaire: View){
        this.selectedColor = color
        val layout = formulaire.findViewById<LinearLayout>(R.id.listCouleurs)
        layout.children.forEach {
            val but = it as Button
            val butCol = but.background as ColorDrawable
            but.text = ""
            if(butCol.color == Color.parseColor(color))
                but.text = "✓"
            else
                but.text = ""
        }
    }

}