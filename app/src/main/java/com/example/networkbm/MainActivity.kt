package com.example.networkbm

import android.annotation.SuppressLint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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

        val tableButsMenu = arrayOf (
            findViewById<ImageButton>(R.id.butAjoutObjet),
            findViewById<ImageButton>(R.id.butAjoutConnexion),
            findViewById<ImageButton>(R.id.butModif)
        )

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setOnClickListener{
                clickMenu(i+1)
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
                    System.out.println("HEYO1")
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isPressed){
                            val dialog = AjoutObjetDialogFragment()
                            dialog.show(supportFragmentManager, "Ajouter un objet")
                        }
                    },500)
                }
                MotionEvent.ACTION_MOVE -> {
                }

                MotionEvent.ACTION_UP -> {
                    isPressed = false
                }

                MotionEvent.ACTION_CANCEL -> {

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

    fun clickMenu(i : Int)
    {
       when(i){
           1 -> {
               this.modeSelected = Mode.AJOUT_OBJET
                ajouterObjetDialog()
           }
           2 -> this.modeSelected = Mode.AJOUT_CONNEXION
           3 -> this.modeSelected = Mode.MODIFICATION
       }
    }

    fun ajouterObjetDialog()
    {
        val dialog = AjoutObjetDialogFragment()
        dialog.show(supportFragmentManager, "Ajouter un objet")
    }

    fun creerObjet(nom: String, couleur: String) : Objet{
        val contPrinc = findViewById<RelativeLayout>(R.id.contPrinc)
        val rect = Rectangle(this)
        rect.createRect(contPrinc)
        this.setDragable(rect)
        rect.scaleX = 5F
        rect.scaleY = 5F
        contPrinc.addView(rect)

        return Objet(nom, couleur, rect)
    }


    fun setDragable(img: View){
        val rootLayout = findViewById<ViewGroup>(R.id.contPrinc)
        val layoutParams = RelativeLayout.LayoutParams(100,100)
        img.layoutParams = layoutParams
        val tlist = TouchDragObject(rootLayout)
        img.setOnTouchListener(tlist)
        rootLayout.invalidate()
    }

    override fun onDeptSelected(depts: ArrayList<String>) {
        when(modeSelected) {
            Mode.AJOUT_OBJET -> {
                var nom = depts.get(0)
                if(depts.size >= 2)  reseau.addObjet(creerObjet(nom, depts.get(1)))
                else reseau.addObjet(creerObjet(nom, "#fff000"))
            }
        }
    }
}