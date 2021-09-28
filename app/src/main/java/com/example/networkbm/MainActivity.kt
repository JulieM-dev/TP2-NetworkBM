package com.example.networkbm

import android.widget.ImageButton

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.models.Mode
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    private var modeSelected : Mode? = null



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
            TODO()
        }

        var tableButsMenu = arrayOf (
            findViewById<ImageButton>(R.id.butAjoutObjet),
            findViewById<ImageButton>(R.id.butAjoutConnexion),
            findViewById<ImageButton>(R.id.butModif)
        )

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu.get(i).setOnClickListener{
                clickMenu(i)
            }
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
           0 -> this.modeSelected = Mode.AJOUT_OBJET
           1 -> this.modeSelected = Mode.AJOUT_CONNEXION
           2 -> this.modeSelected = Mode.MODIFICATION
       }
        Toast.makeText(this@MainActivity, this.modeSelected?.getLibelle(), Toast.LENGTH_LONG).show()
    }
}