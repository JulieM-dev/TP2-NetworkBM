package com.example.networkbm

import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.fragments.AjoutObjetDialogFragment
import com.example.networkbm.models.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), DeptListener {

    lateinit var toggle: ActionBarDrawerToggle

    private var modeSelected : Mode = Mode.AUCUN

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

    fun setImage(){
        val img = findViewById<ImageView>(R.id.planAppartement)
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
                ajouterObjet()
           }
           2 -> this.modeSelected = Mode.AJOUT_CONNEXION
           3 -> this.modeSelected = Mode.MODIFICATION
       }
        Toast.makeText(this@MainActivity, this.modeSelected.getLibelle(), Toast.LENGTH_LONG).show()
    }

    fun ajouterObjet()
    {
        val dialog = AjoutObjetDialogFragment()
        dialog.show(supportFragmentManager, "Ajouter un objet")
    }

    fun ajoutRectangle() : Rectangle{
        val contPrinc = findViewById<RelativeLayout>(R.id.contPrinc)
        val rect = Rectangle(this)
        rect.createRect(contPrinc)

        this.setDragable(rect)
        contPrinc.addView(rect)

        rect.changeColor("#fff000")

        return rect
    }

    fun setDragable(img: View){
        val rootLayout = findViewById<ViewGroup>(R.id.contPrinc)
        val layoutParams = RelativeLayout.LayoutParams(100,100)
        img.layoutParams = layoutParams
        val tlist = TouchDragObject(rootLayout)
        img.setOnTouchListener(tlist)
        rootLayout.invalidate()
    }

    override fun onDeptSelected(dept: String) {
        if(modeSelected == Mode.AJOUT_OBJET)
        {
            reseau.addObjet(Objet(dept, ajoutRectangle()))
        }

    }
}