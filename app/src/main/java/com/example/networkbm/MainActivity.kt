package com.example.networkbm

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.networkbm.models.Mode

class MainActivity : AppCompatActivity() {

    private var modeSelected : Mode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_raccourci)

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