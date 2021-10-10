package com.example.networkbm.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.get
import com.example.networkbm.DeptListener
import com.example.networkbm.R
import com.example.networkbm.models.Connexion
import com.example.networkbm.models.Graph
import com.example.networkbm.models.Objet
import java.lang.ClassCastException

class EditConnexionDialogFragment() : AppCompatDialogFragment(), AdapterView.OnItemSelectedListener {
    lateinit var listener: DeptListener
    lateinit var dialogBuilder : AlertDialog.Builder
    lateinit var alertDialog : AlertDialog
    lateinit var buttonValider : Button
    lateinit var buttonAnnuler : Button
    lateinit var buttonSupprimer : Button
    lateinit var editObjet1 : Spinner
    lateinit var editObjet2 : Spinner
    var connexion : Connexion? = null
    var reseau : Graph? = null
    var obj1 : Objet? = null
    var obj2: Objet? = null
    var idT1 = 0
    var idT2 = 0

    constructor(connexion: Connexion, reseau: Graph) : this() {
        this.connexion = connexion
        this.reseau = reseau
        this.obj1 = connexion.getObjet1()
        this.obj2 = connexion.getObjet2()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)

        var formulaire = activity?.layoutInflater?.inflate(R.layout.edit_connexion_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {
            if(connexion != null)
            {

            }
            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)
            buttonSupprimer = formulaire.findViewById(R.id.buttonSupprimer)
            editObjet1 = formulaire.findViewById(R.id.editObjet1)
            editObjet2 = formulaire.findViewById(R.id.editObjet2)

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
                alertDialog.dismiss()
            }
            buttonSupprimer.setOnClickListener()
            {
                val depts = ArrayList<String>()
                reseau!!.connexions.remove(connexion)
                Toast.makeText(this.context, getString(R.string.connectionDeleted), Toast.LENGTH_SHORT).show()
                listener.onDeptSelected(depts)
                alertDialog.dismiss()
            }

        }

        return alertDialog
    }

    fun setList(){
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

    fun valider(){
        val depts = ArrayList<String>()
        Toast.makeText(this.context, getString(R.string.connectionModified), Toast.LENGTH_SHORT).show()

        val o1 = this.reseau!!.getObjet(this.obj1!!.centerX(), this.obj1!!.centerY())
        val o2 = this.reseau!!.getObjet(this.obj2!!.centerX(), this.obj2!!.centerY())

        connexion!!.setObjet1(o1!!)
        connexion!!.setObjet2(o2!!)
        listener.onDeptSelected(depts)
        alertDialog.dismiss()
    }

}