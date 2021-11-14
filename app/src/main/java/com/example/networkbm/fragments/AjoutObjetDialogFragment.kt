package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    private lateinit var listener: DeptListener
    private lateinit var dialogBuilder : AlertDialog.Builder
    private lateinit var alertDialog : AlertDialog
    private lateinit var editTextNom : EditText
    private lateinit var buttonValider : Button
    private lateinit var buttonAnnuler : Button
    private lateinit var buttonSupprimer : Button
    var objet : Objet? = null
    var reseau : Graph? = null
    private var listCouleurs = arrayOf("#2d3436","#e74c3c","#2ecc71","#3498db","#e67e22","#00cec9","#9b59b6")
    private var listIcones = arrayOf(null, "printer", "blender", "camera", "computer", "lamp", "television")
    private var selectedColor : String? = null
    private var selectedIcon: String? = null

    constructor(objet: Objet, reseau: Graph) : this() {
        this.objet = objet
        this.reseau = reseau
    }

    /**
     * On initialise la fenêtre de dialogue pour modifier / créer un objet
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)
        this.selectedColor = this.listCouleurs[0]

        val formulaire = activity?.layoutInflater?.inflate(R.layout.ajout_objet_form, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if (formulaire != null) {

            buttonValider = formulaire.findViewById(R.id.buttonValider)
            buttonAnnuler = formulaire.findViewById(R.id.buttonAnnuler)
            buttonSupprimer = formulaire.findViewById(R.id.buttonSupprimer)

            //Liste des boutons de couleur
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

            //Liste des boutons d'icones
            val layoutIcon = formulaire.findViewById<LinearLayout>(R.id.listIcones)
            val butNoirIcon = formulaire.findViewById<ImageButton>(R.id.butIcon1)
            listIcones.forEach {
                val newBut = ImageButton(buttonValider.context)
                newBut.layoutParams = butNoir.layoutParams

                var path = this.resources.getIdentifier("cross", "drawable", this.context!!.packageName)
                if(it != null){
                    path = this.resources.getIdentifier(it.toString(), "drawable", this.context!!.packageName)
                    newBut.setBackgroundColor(resources.getColor(R.color.black))
                } else {
                    newBut.setBackgroundColor(Color.parseColor("#2ecc71"))
                }

                var icon2 = BitmapFactory.decodeResource(this.resources, path)
                icon2 = Bitmap.createScaledBitmap(icon2, 100, 100, false)
                newBut.setImageBitmap(icon2)
                newBut.tag = it.toString()
                newBut.setOnClickListener()
                {
                    this.clickIcon(newBut.tag.toString(), formulaire)
                }
                layoutIcon.addView(newBut)
            }
            layoutIcon.removeView(butNoirIcon)

            editTextNom = formulaire.findViewById(R.id.editTextNom)
            if(objet != null)
            {
                editTextNom.setText(objet!!.nom)
                this.selectedColor = objet!!.couleur
                this.clickCouleur(objet!!.couleur!!, formulaire)
                if(objet!!.icone != null){
                    this.selectedIcon = objet!!.icone
                    this.clickIcon(objet!!.icone!!, formulaire)
                }
            } else {
                this.clickCouleur(this.listCouleurs[0], formulaire)
            }

            buttonValider.setOnClickListener()
            {
                this.valider()
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
                    val depts = ArrayList<String>()
                    var i = 0
                    val si = reseau!!.connexions.size
                    val con = this.reseau!!.connexions.clone() as ArrayList<Connexion>
                    while(i < si){
                        val itCon = con[i]
                        if(itCon.getObjet1() == objet || itCon.getObjet2() == objet){
                            reseau!!.connexions.remove(itCon)
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

    /**
     * On modifie la couleur
     */
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

    /**
     * On modifie l'icône
     */
    private fun clickIcon(tag: String, formulaire: View){
        this.selectedIcon = tag
        val layout = formulaire.findViewById<LinearLayout>(R.id.listIcones)
        layout.children.forEach {
            val but = it as ImageButton
            val butCol = but.tag.toString()
            if(butCol == tag)
                but.setBackgroundColor(Color.parseColor("#2ecc71"))
            else
                but.setBackgroundColor(Color.parseColor("black"))
        }
    }

    /**
     * On valide la modification
     */
    private fun valider(){
        val nom = editTextNom.text.toString()
        val depts = ArrayList<String>()
        depts.add(nom)
        depts.add(this.selectedColor!!)
        if(this.selectedIcon !== null) {
            depts.add(this.selectedIcon!!)
        }
        listener.onDeptSelected(depts)
        alertDialog.dismiss()
    }

    /**
     * On relie la fenêtre de dialogue au contexte principal
     */
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