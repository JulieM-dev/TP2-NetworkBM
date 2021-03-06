package com.example.networkbm.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.networkbm.DeptListener
import com.example.networkbm.R
import java.lang.ClassCastException

class ChangePlanFragment : AppCompatDialogFragment() {
    private lateinit var dialogBuilder : AlertDialog.Builder
    private lateinit var alertDialog : AlertDialog
    private var listPlans = arrayOf("planmin", "planmin2", "planmin3", "planmin4")
    private lateinit var listener: DeptListener

    /**
     * On initialise la fenêtre de dialogue pour changer le plan
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBuilder =  AlertDialog.Builder(activity)
        val formulaire = activity?.layoutInflater?.inflate(R.layout.liste_plans, null)
        dialogBuilder.setView(formulaire)
            .setTitle(this.tag)

        alertDialog = dialogBuilder.create()

        if(formulaire != null){

            //Liste des plans
            val layoutPlan = formulaire.findViewById<LinearLayout>(R.id.listPlans)
            val butPlan = formulaire.findViewById<ImageButton>(R.id.butPlan1)
            listPlans.forEach {
                val newBut = ImageButton(butPlan.context)
                newBut.layoutParams = butPlan.layoutParams

                val path = this.resources.getIdentifier(it, "drawable", this.context!!.packageName)
                newBut.setBackgroundColor(Color.parseColor("#f2f2f2"))

                val img = BitmapFactory.decodeResource(this.resources, path)
                newBut.setImageBitmap(img)
                newBut.tag = it
                newBut.scaleType = ImageView.ScaleType.CENTER_INSIDE
                newBut.setOnClickListener()
                {
                    this.clickImg(newBut.tag.toString())
                }
                layoutPlan.addView(newBut)
            }
            layoutPlan.removeView(butPlan)
        }

        return alertDialog
    }

    /**
     * On choisit un plan
     */
    private fun clickImg(tag: String){
        val depts = ArrayList<String>()
        depts.add("changePlan")
        depts.add(tag)
        Toast.makeText(this.context, getString(R.string.planChanged), Toast.LENGTH_SHORT).show()
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