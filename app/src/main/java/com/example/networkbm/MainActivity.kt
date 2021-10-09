package com.example.networkbm

import android.annotation.SuppressLint
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.fragments.AjoutObjetDialogFragment
import com.example.networkbm.fragments.EditConnexionDialogFragment
import com.example.networkbm.models.*
import com.google.android.material.navigation.NavigationView
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), DeptListener {

    lateinit var toggle: ActionBarDrawerToggle
    private var modeSelected : Mode = Mode.AUCUN
    private var isPressed = false
    private lateinit var objetAModifier : Objet
    var drawGraph = DrawableGraph()
    var reseau = drawGraph.reseau
    var connexionAModifier : Connexion? = null
    lateinit var tableButsMenu : Array<ImageButton>
    private var savePosX = 0F
    private var savePosY = 0F
    lateinit var ecran : ImageView
    lateinit var dragOnTouch : TouchDragObject
    private lateinit var affPrinc : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        this.affPrinc = findViewById(R.id.affPrinc)
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
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setOnClickListener{
                clickMenu(i+1)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein))
            }
        }
        ecran = findViewById<ImageView>(R.id.contRect)
        ecran.setImageDrawable(drawGraph)
        var plan = findViewById<ImageView>(R.id.planAppartement)
        ecran.setBackgroundColor(Color.BLUE)
        ecran.minimumHeight = plan.height
        ecran.maxHeight = plan.height
        ecran.minimumWidth = plan.width
        ecran.maxWidth = plan.width
        dragOnTouch = TouchDragObject(ecran, reseau)
        var obj1 = Objet(this, "test", 320F, 620F)
        var obj2 = Objet(this, "test", 731F, 1378F)
        reseau.objets.add(obj1)
        reseau.objets.add(obj2)
        var connexion = Connexion(obj1, reseau, this)
        connexion.setObjet2(obj2)
        reseau.connexions.add(connexion)

        initListeners()
    }


    fun reinitialisation()
    {
        reseau.connexions.clear()
        reseau.objets.clear()
        ecran.invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initListeners()
    {


        ecran.setOnTouchListener { v, event ->
            dragOnTouch.onTouch(null, event)
            dragOnTouch.dragLine(null, event)
            val action = event.action
            when(action)
            {
                MotionEvent.ACTION_DOWN ->
                {
                    this.savePosX = event.getX()
                    this.savePosY = event.getY()

                    if(this.modeSelected != Mode.AJOUT_OBJET)
                    {
                        //On n'est pas en mode ajout d'objet, il y a donc des actions a gerer
                        var objet = reseau.getObjet(event.x, event.y)
                        var connexion = reseau.getConnexion(event.x, event.y)
                        if(connexion != null && modeSelected == Mode.MODIFICATION)
                        {
                            //On a recupere une connexion
                            val dialog = EditConnexionDialogFragment(connexion, reseau)
                            dialog.show(supportFragmentManager, resources.getString(R.string.editConnection))
                        } else if(objet != null)
                        {
                            //On a recupere un objet
                            when(modeSelected)
                            {
                                Mode.AUCUN -> {
                                    //On n'est dans aucun mode, on peut bouger l'objet
                                    dragOnTouch.onTouch(objet, event)
                                }
                                Mode.AJOUT_CONNEXION -> {
                                    //On est en mode ajout de connexion, on met le systeme de connexion entre objets
                                    if(connexionAModifier == null)
                                    {
                                        connexionAModifier = Connexion(objet, reseau, this)
                                        reseau.connexions.add(connexionAModifier!!)
                                        dragOnTouch.dragLine(connexionAModifier!!, event)
                                    }
                                    else if(connexionAModifier != null)
                                    {
                                        dragOnTouch.dragLine(connexionAModifier!!, event)
                                    }
                                }
                                Mode.MODIFICATION -> {
                                    //On met en mode modification, on affiche la fenetre de modification
                                    objetAModifier = objet
                                    val dialog = AjoutObjetDialogFragment(objet, reseau)
                                    dialog.show(supportFragmentManager, resources.getString(R.string.editObject))
                                }
                            }
                        }
                        else if (modeSelected == Mode.AJOUT_CONNEXION && connexionAModifier != null)
                        {
                            dragOnTouch.dragLine(connexionAModifier!!, event)
                        }
                        else
                        {
                            isPressed = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (isPressed &&
                                        event.x > this.savePosX - 20 &&
                                        event.x < this.savePosX + 20) {
                                    objetAModifier = Objet(this, "unnamed" , savePosX, savePosY)
                                    val dialog = AjoutObjetDialogFragment()
                                    dialog.show(supportFragmentManager, resources.getString(R.string.addObject))
                                    this.clickMenu(1)
                                }
                            }, 1000)
                        }
                    }
                    else
                    {
                        objetAModifier = Objet(this, "unnamed" , event.getX(), event.getY())
                        val dialog = AjoutObjetDialogFragment()
                        dialog.show(supportFragmentManager, resources.getString(R.string.addObject))
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isPressed = false
                    if(modeSelected == Mode.AJOUT_CONNEXION && connexionAModifier != null)
                    {
                        var objet = reseau.getObjet(connexionAModifier!!.pointerX, connexionAModifier!!.pointerY)
                        if(objet != null && objet != connexionAModifier!!.getObjet1())
                        {
                            connexionAModifier!!.setObjet2(objet)
                            Toast.makeText(this, getString(R.string.connectionCreated), LENGTH_SHORT).show()
                        }
                        else
                        {
                            reseau.connexions.remove(connexionAModifier)
                        }
                        connexionAModifier = null

                    }
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
        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
       when(i){
           1 -> {
               if(this.modeSelected != Mode.AJOUT_OBJET)
               {
                   this.modeSelected = Mode.AJOUT_OBJET
                   drawGraph.alpha = 0
                   this.affPrinc.text = resources.getString(R.string.addObject)
               }
               else
               {
                   this.modeSelected = Mode.AUCUN
                   this.affPrinc.text = resources.getString(R.string.pressScreen)
                   drawGraph.alpha = 255
               }

               //ajouterObjetDialog()
           }
           2 -> {
               if(this.modeSelected != Mode.AJOUT_CONNEXION) {
                   this.modeSelected = Mode.AJOUT_CONNEXION
                   this.affPrinc.text = resources.getString(R.string.addConnection)
               }
               else {
                   this.modeSelected = Mode.AUCUN
                   this.affPrinc.text = resources.getString(R.string.pressScreen)
               }
           }
           3 -> {
               if(this.modeSelected != Mode.MODIFICATION) {
                   this.modeSelected = Mode.MODIFICATION
                   this.affPrinc.text = resources.getString(R.string.editObject)
               }
               else {
                   this.modeSelected = Mode.AUCUN
                   this.affPrinc.text = resources.getString(R.string.pressScreen)
               }

           }
       }
        if(this.modeSelected != Mode.AUCUN)
            tableButsMenu.get(i - 1).setBackgroundColor(Color.parseColor("#fff000"))
        else
            tableButsMenu.get(i - 1).setBackgroundColor(Color.parseColor("#ffffff"))

        ecran.invalidate()
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
        //Click sur une page de dialogue
        when(modeSelected) {
            Mode.AJOUT_OBJET -> {
                objetAModifier.nom = depts.get(0)
                reseau.objets.add(objetAModifier)
                Toast.makeText(this, getString(R.string.objectCreated), LENGTH_SHORT).show()
            }
            Mode.MODIFICATION -> {
                val obj = reseau.getObjet(this.savePosX, this.savePosY)
                if(obj != null){
                    obj!!.nom = depts.get(0)
                    Toast.makeText(this, getString(R.string.objectModified), LENGTH_SHORT).show()
                }
            }
        }
        ecran.invalidate()
    }



}