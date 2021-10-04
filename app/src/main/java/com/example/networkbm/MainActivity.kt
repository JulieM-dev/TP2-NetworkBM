package com.example.networkbm

import android.annotation.SuppressLint
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.fragments.AjoutObjetDialogFragment
import com.example.networkbm.models.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), DeptListener {

    lateinit var toggle: ActionBarDrawerToggle
    private var modeSelected : Mode = Mode.AUCUN
    private var isPressed = false
    private lateinit var objetAModifier : Objet
    var drawGraph = DrawableGraph()
    var reseau = drawGraph.reseau
    var connexionAModifier : Connexion? = null
    lateinit var tableButsMenu : Array<ImageButton>
    private var savePosX = 0
    private var savePosY = 0
    lateinit var ecran : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.navView)

        tableButsMenu = arrayOf (
            findViewById<ImageButton>(R.id.butAjoutObjet),
            findViewById<ImageButton>(R.id.butAjoutConnexion),
            findViewById<ImageButton>(R.id.butModif)
        )

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.button_reinitialiser -> reinitialisation()
                R.id.button_ajout_objet -> clickMenu(1)
                R.id.button_ajout_connexion -> clickMenu(2)
                R.id.button_modification -> clickMenu(3)
            }
            true
        }

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setBackgroundColor(Color.parseColor("#ffffff"))
            tableButsMenu.get(i).setOnClickListener{
                clickMenu(i+1)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein))
            }
        }
        ecran = findViewById<ImageView>(R.id.contRect)
        ecran.setImageDrawable(drawGraph)
        var obj1 = Objet(this, "test", 300F, 600F)
        var obj2 = Objet(this, "test", 900F, 600F)
        reseau.objets.add(obj1)
        reseau.objets.add(obj2)
        var connexion = Connexion(obj1, reseau, this)
        connexion.setObjet2(obj2)
        reseau.connexions.add(connexion)


        setImage()
    }

    fun reinitialisation()
    {

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setImage(){
        ecran.setOnTouchListener { v, event ->
            val action = event.action
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    this.savePosX = event.getX().toInt()
                    this.savePosY = event.getY().toInt()
                    if(this.modeSelected != Mode.AJOUT_OBJET) {
                        isPressed = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (isPressed) {
                                val dialog = AjoutObjetDialogFragment()
                                dialog.show(supportFragmentManager, "Ajouter un objet")
                                this.clickMenu(1)
                            }
                        }, 500)
                    } else {
                        val dialog = AjoutObjetDialogFragment()
                        dialog.show(supportFragmentManager, "Ajouter un objet")
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isPressed = false
                }
                else ->{

                }
            }
            ecran.invalidate()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun clickMenu(i: Int)
    {

       when(i){
           1 -> {
               if(this.modeSelected != Mode.AJOUT_OBJET)
                    this.modeSelected = Mode.AJOUT_OBJET
               else
                   this.modeSelected = Mode.AUCUN
               //ajouterObjetDialog()
           }
           2 -> {
               if(this.modeSelected != Mode.AJOUT_CONNEXION) {
                   this.modeSelected = Mode.AJOUT_CONNEXION
                   ajoutConnexion()
               }
               else {
                   this.modeSelected = Mode.AUCUN
               }
           }
           3 -> {
               if(this.modeSelected != Mode.MODIFICATION) {
                   this.modeSelected = Mode.MODIFICATION
               }
               else {
                   this.modeSelected = Mode.AUCUN
               }

           }
       }
        if(this.modeSelected != Mode.AUCUN)
            tableButsMenu.get(i - 1).setBackgroundColor(Color.parseColor("#fff000"))
        else
            tableButsMenu.get(i - 1).setBackgroundColor(Color.parseColor("#ffffff"))
    }

    fun ajouterObjetDialog()
    {
        val dialog = AjoutObjetDialogFragment()
        dialog.show(supportFragmentManager, "Ajouter un objet")
    }


    fun ajoutConnexion()
    {

    }

    override fun onDeptSelected(depts: ArrayList<String>) {
        when(modeSelected) {
            Mode.AJOUT_OBJET -> {
                var nom = depts.get(0)
                reseau.objets.add(Objet(this, nom, 300F, 300F))
                ecran.invalidate()
            }
        }
    }
}