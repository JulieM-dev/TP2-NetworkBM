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
    var reseau = Reseau()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = findViewById<NavigationView>(R.id.navView)

        val tableButsMenu = arrayOf (
            findViewById<ImageButton>(R.id.butAjoutObjet),
            findViewById<ImageButton>(R.id.butAjoutConnexion),
            findViewById<ImageButton>(R.id.butModif)
        )

        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.button_reinitialiser -> reinitialisation()
                R.id.button_ajout_objet -> clickMenu(1, tableButsMenu)
                R.id.button_ajout_connexion -> clickMenu(2, tableButsMenu)
                R.id.button_modification -> clickMenu(3, tableButsMenu)
            }
            true
        }

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setOnClickListener{
                clickMenu(i+1, tableButsMenu)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein))
            }
        }

        setImage()
    }

    fun reinitialisation()
    {

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setImage(){
        val img = findViewById<ImageView>(R.id.planAppartement)
        img.setOnTouchListener { v, event ->
            val action = event.action
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    isPressed = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isPressed){
                            val dialog = AjoutObjetDialogFragment()
                            dialog.show(supportFragmentManager, "Ajouter un objet")
                            this.modeSelected = Mode.AJOUT_OBJET
                        }
                    },500)
                }
                else ->{

                }
            }
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

    fun clickMenu(i: Int, tableButsMenu: Array<ImageButton>)
    {
        for(i in 0..tableButsMenu.size-1) {
            tableButsMenu.get(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
       when(i){
           1 -> {
               if(this.modeSelected != Mode.AJOUT_OBJET)
                    this.modeSelected = Mode.AJOUT_OBJET
               else
                   this.modeSelected = Mode.AUCUN
               //ajouterObjetDialog()
           }
           2 -> {
               if(this.modeSelected != Mode.AJOUT_CONNEXION)
                    this.modeSelected = Mode.AJOUT_CONNEXION
               else
                   this.modeSelected = Mode.AUCUN
           }
           3 -> {
               if(this.modeSelected != Mode.MODIFICATION)
                    this.modeSelected = Mode.MODIFICATION
               else
                   this.modeSelected = Mode.AUCUN
               initModificationListener()
           }
       }
        if(this.modeSelected != Mode.AUCUN)
            tableButsMenu.get(i - 1).setBackgroundColor(Color.parseColor("#fff000"))
    }

    fun ajouterObjetDialog()
    {
        val dialog = AjoutObjetDialogFragment()
        dialog.show(supportFragmentManager, "Ajouter un objet")
    }

    fun initModificationListener()
    {
        reseau.objets.forEach{
            it.setDragable(false)
        }
    }

    fun creerObjet(nom: String, couleur: String) : Objet{
        val contPrinc = findViewById<RelativeLayout>(R.id.contPrinc)
        val objet = Objet(this, nom, couleur)
        objet.createRect(contPrinc)
        this.setDragable(objet)
        objet.scaleX = 1.5F
        objet.scaleY = 1.5F
        contPrinc.addView(objet)
        var anim : Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        objet.startAnimation(anim)
        objet.setOnClickListener{
                if(modeSelected == Mode.MODIFICATION)
                {
                    objetAModifier = it as Objet
                    val dialog = AjoutObjetDialogFragment(objetAModifier)
                    dialog.show(supportFragmentManager, "Modifier un objet")
                    it.setDragable(true)
                }
        }
        return objet
    }



    fun setDragable(objet: Objet){
        val rootLayout = findViewById<ViewGroup>(R.id.contPrinc)
        val layoutParams = RelativeLayout.LayoutParams(100,100)
        objet.layoutParams = layoutParams
        val tlist = TouchDragObject(rootLayout)
        objet.addTouchDragObject(tlist)
        rootLayout.invalidate()
    }

    override fun onDeptSelected(depts: ArrayList<String>) {
        when(modeSelected) {
            Mode.AJOUT_OBJET -> {
                var nom = depts.get(0)
                if(depts.size >= 2)  reseau.addObjet(creerObjet(nom, depts.get(1)))
                else reseau.addObjet(creerObjet(nom, "#fff000"))
            }
            Mode.MODIFICATION -> {
                if(objetAModifier != null)
                {
                    objetAModifier.clear()
                    objetAModifier.nom = depts.get(0)
                    objetAModifier.editRect(findViewById<RelativeLayout>(R.id.contPrinc))
                }


            }
        }
    }
}