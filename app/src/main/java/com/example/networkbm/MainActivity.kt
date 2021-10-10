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
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.fragments.AjoutObjetDialogFragment
import com.example.networkbm.fragments.EditConnexionDialogFragment
import com.example.networkbm.models.*
import com.example.networkbm.views.WScrollView
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
    var dragging = false

    private lateinit var affPrinc : TextView
    lateinit var hsv : WScrollView

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
        var plan = findViewById<ImageView>(R.id.planAppartement)

        System.out.println("-----------------------------HEIGH " +  plan.measuredHeight)

        ecran.setImageDrawable(drawGraph)

        var frame = findViewById<FrameLayout>(R.id.frameLayout)

        var obj1 = Objet(this, "test", 320F, 620F)
        var obj2 = Objet(this, "test", 731F, 1378F)
        reseau.objets.add(obj1)
        reseau.objets.add(obj2)
        var connexion = Connexion(obj1, reseau, this)
        connexion.setObjet2(obj2)
        reseau.connexions.add(connexion)

        // Bidirectionnal scrollview
        var sv = findViewById<ScrollView>(R.id.scrollView)
        hsv = findViewById<WScrollView>(R.id.scrollViewH)
        hsv.sv = sv
        hsv.ecran = ecran
        dragOnTouch = TouchDragObject(hsv, reseau)
        plan.doOnPreDraw {
            System.out.println("-----------------------------plan HEIGHT " +  plan.height)
            System.out.println("-----------------------------plan WIDTH " +  plan.width)
            System.out.println("-----------------------------hsv HEIGHT " +  hsv.height)
            System.out.println("-----------------------------hsv WIDTH " +  hsv.width)
            ecran.layoutParams.height = plan.height
            ecran.layoutParams.width = plan.width
            ecran.invalidate()
            System.out.println("-----------------------------HEIGH " +  ecran.layoutParams.height)
        }


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
        hsv.setOnTouchListener { view, event ->
            System.out.println("ON TOUCH HSV")
            ecran.dispatchTouchEvent(event)

            true
        }

        ecran.setOnTouchListener { v, event ->
            System.out.println("ON TOUCH ECRAN")
            this.savePosX = event.getX() + hsv.scrollX
            this.savePosY = event.getY() + hsv.sv.scrollY
            var draggingObj = dragOnTouch.onTouch(null, event, savePosX, savePosY)
            var draggingLine = dragOnTouch.dragLine(null, event, savePosX, savePosY)
            dragging = draggingLine || draggingObj
            System.out.println(dragging)
            val action = event.action
            System.out.println(savePosX.toString() + " " + savePosY)
            when(action)
            {
                MotionEvent.ACTION_DOWN ->
                {


                    if(this.modeSelected != Mode.AJOUT_OBJET)
                    {
                        //On n'est pas en mode ajout d'objet, il y a donc des actions a gerer
                        var objet = reseau.getObjet(savePosX, savePosY)
                        var connexion = reseau.getConnexion(savePosX, savePosY)
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
                                    System.out.println("before dragg")
                                    dragging = dragOnTouch.onTouch(objet, event, savePosX, savePosY)

                                }
                                Mode.AJOUT_CONNEXION -> {
                                    //On est en mode ajout de connexion, on met le systeme de connexion entre objets
                                    if(connexionAModifier == null)
                                    {
                                        connexionAModifier = Connexion(objet, reseau, this)
                                        reseau.connexions.add(connexionAModifier!!)
                                        dragging = dragOnTouch.dragLine(connexionAModifier!!, event, savePosX, savePosY)
                                    }
                                    else if(connexionAModifier != null)
                                    {
                                        dragging = dragOnTouch.dragLine(connexionAModifier!!, event, savePosX, savePosY)
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
                            dragging = dragOnTouch.dragLine(connexionAModifier!!, event, savePosX, savePosY)
                        }
                        else
                        {
                            isPressed = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (isPressed &&
                                        event.x + hsv.scrollX > this.savePosX - 20 &&
                                        event.x + hsv.scrollX < this.savePosX + 20) {
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
                        objetAModifier = Objet(this, "unnamed" , savePosX, savePosY)
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
            if(!dragging)
            {
                ecran.invalidate()
                hsv.onTouchEvent(event)
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