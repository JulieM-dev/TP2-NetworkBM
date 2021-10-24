package com.example.networkbm

import android.annotation.SuppressLint
import android.content.Intent

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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

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
    private var saveScrollX : Int? = null
    private var saveScrollY : Int? = null
    lateinit var ecran : ImageView
    lateinit var dragOnTouch : TouchDragObject
    var dragging = false
    var saveReseau = SaveReseau()

    lateinit var locale: Locale
    private var currentLanguage = "fr"
    private var currentLang: String? = null

    var handler : Handler? = null

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
        navView.getHeaderView(0).setBackgroundColor(resources.getColor(R.color.purple_princ))

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
                R.id.switch_lang -> changeLanguage()
                R.id.saveReseau -> saveReseau()
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
        val plan = findViewById<ImageView>(R.id.planAppartement)
        ecran = findViewById<ImageView>(R.id.contRect)

        ecran.setImageDrawable(drawGraph)


        // Bidirectionnal scrollview
        val sv = findViewById<ScrollView>(R.id.scrollView)
        hsv = findViewById<WScrollView>(R.id.scrollViewH)
        hsv.sv = sv
        hsv.ecran = ecran
        dragOnTouch = TouchDragObject(hsv, reseau, plan)
        plan.doOnPreDraw {
            val param: FrameLayout.LayoutParams = FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,100);
            param.height = plan.height
            param.width = plan.width
            ecran.layoutParams = param
            ecran.invalidate()
        }

        handler = Handler(Looper.getMainLooper())

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
            ecran.dispatchTouchEvent(event)

            true
        }

        ecran.setOnLongClickListener {
            true
        }

        ecran.setOnTouchListener { v, event ->
            this.savePosX = event.getX() + hsv.scrollX
            this.savePosY = event.getY() + hsv.sv.scrollY
            val draggingObj = dragOnTouch.onTouch(null, event, savePosX, savePosY)
            val draggingLine = dragOnTouch.dragLine(null, event, savePosX, savePosY)
            dragging = draggingLine || draggingObj
            val action = event.action
            when(action)
            {
                MotionEvent.ACTION_DOWN ->
                {
                    if(this.modeSelected != Mode.AJOUT_OBJET)
                    {
                        //On n'est pas en mode ajout d'objet, il y a donc des actions a gerer
                        val objet = reseau.getObjet(savePosX, savePosY)
                        val connexion = reseau.getConnexion(savePosX, savePosY)
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
                                    dragging = dragOnTouch.onTouch(objet, event, savePosX, savePosY)

                                }
                                Mode.AJOUT_CONNEXION -> {
                                    //On est en mode ajout de connexion, on met le systeme de connexion entre objets
                                    if(connexionAModifier == null)
                                    {
                                        connexionAModifier = Connexion(objet, reseau)
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
                                else -> null
                            }
                        }
                        else if (modeSelected == Mode.AJOUT_CONNEXION && connexionAModifier != null)
                        {
                            dragging = dragOnTouch.dragLine(connexionAModifier!!, event, savePosX, savePosY)
                        }
                        else
                        {
                            if(saveScrollX == null)
                            {
                                saveScrollX = hsv.scrollX
                                saveScrollY = hsv.scrollY
                            }

                            isPressed = true

                            handler!!.postDelayed({
                                if (isPressed &&
                                    hsv.scrollX > saveScrollX!! - 10 &&
                                    hsv.scrollX < saveScrollX!! + 10 &&
                                    hsv.scrollY > saveScrollY!! - 10 &&
                                    hsv.scrollY < saveScrollY!! + 10
                                ) {
                                    this.isPressed = false
                                    objetAModifier = Objet("unnamed" , savePosX, savePosY)
                                    val dialog = AjoutObjetDialogFragment()
                                    dialog.show(supportFragmentManager, resources.getString(R.string.addObject))
                                    this.clickMenu(1)
                                }
                            }, 2000)


                        }
                    }
                    else
                    {
                        objetAModifier = Objet("unnamed" , savePosX, savePosY)
                        val dialog = AjoutObjetDialogFragment()
                        dialog.show(supportFragmentManager, resources.getString(R.string.addObject))
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isPressed = false

                    handler = Handler(Looper.getMainLooper())

                    saveScrollX = null
                    saveScrollY = null
                    if(modeSelected == Mode.AJOUT_CONNEXION && connexionAModifier != null)
                    {
                        val objet = reseau.getObjet(connexionAModifier!!.pointerX, connexionAModifier!!.pointerY)
                        if(objet != null && objet != connexionAModifier!!.getObjet1() && !existeConnexion(connexionAModifier!!.getObjet1(), objet))
                        {
                            //Création de la connexion
                            connexionAModifier!!.setObjet2(objet)

                            val dialog = EditConnexionDialogFragment(connexionAModifier!!, reseau, true)
                            dialog.show(supportFragmentManager, resources.getString(R.string.editConnection))

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
        this.saveReseau.read(this, this.reseau)
    }

    private fun existeConnexion(objet1: Objet, objet2: Objet): Boolean {
        var ci = 0
        this.reseau.connexions.forEach{
            if( (it.getObjet1() == objet1 && it.getObjet2() == objet2) || (it.getObjet1() == objet2 && it.getObjet2() == objet1) ){
                ci++
            }
        }
        return ci > 1
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
        val navView = findViewById<NavigationView>(R.id.navView)
        for(it in 0..tableButsMenu.size-1){
            tableButsMenu.get(it).setBackgroundColor(resources.getColor(R.color.white))
            navView.menu.getItem(it + 1).setChecked(false)
        }
        navView.menu.getItem(i).setChecked(true)
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
            tableButsMenu.get(i - 1).setBackgroundColor(resources.getColor(R.color.purple_sec))
        else
        {
            tableButsMenu.get(i - 1).setBackgroundColor(resources.getColor(R.color.white))
            navView.menu.getItem(i).setChecked(false)
        }

        ecran.invalidate()
    }


    override fun onDeptSelected(depts: ArrayList<String>) {
        //Click sur une page de dialogue
        when(modeSelected) {
            Mode.AJOUT_OBJET -> {
                objetAModifier.nom = depts.get(0)
                objetAModifier.couleur = depts.get(1)
                reseau.objets.add(objetAModifier)
                Toast.makeText(this, getString(R.string.objectCreated), LENGTH_SHORT).show()
            }
            Mode.MODIFICATION -> {
                val obj = reseau.getObjet(this.savePosX, this.savePosY)
                if(obj != null){
                    obj.nom = depts.get(0)
                    obj.couleur = depts.get(1)
                    Toast.makeText(this, getString(R.string.objectModified), LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
        ecran.invalidate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("objets", reseau.objets)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        reseau.objets = savedInstanceState.getParcelableArrayList<Objet>("objets") as ArrayList<Objet>
        reseau.objets.forEach {
            it.connexions.forEach{
                if(!existeConnexion(it.getObjet1(), it.getObjet2()!!))
                {
                    reseau.connexions.add(it)
                }
            }
        }
        ecran.invalidate()
    }

    fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@MainActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)
    }

    fun changeLanguage(){
        if(this.currentLanguage == "fr"){
            this.setLocale("en")
        } else {
            this.setLocale("fr")
        }
    }

    fun saveReseau(){
        this.saveReseau.create(this, this.reseau)
        Toast.makeText(this, getString(R.string.networkSaved), LENGTH_SHORT).show()
    }

}