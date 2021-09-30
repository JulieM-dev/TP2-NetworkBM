package com.example.networkbm

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.models.ChoiceTouchListener
import com.example.networkbm.models.Mode
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    private var modeSelected : Mode = Mode.AUCUN



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

        testRectangle()
        testRectangle2()
    }

    fun reinitialisation()
    {

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
           1 -> this.modeSelected = Mode.AJOUT_OBJET
           2 -> this.modeSelected = Mode.AJOUT_CONNEXION
           3 -> this.modeSelected = Mode.MODIFICATION
       }
        Toast.makeText(this@MainActivity, this.modeSelected.getLibelle(), Toast.LENGTH_LONG).show()
    }

    fun testRectangle(){
        var imageView = findViewById<ImageView>(R.id.contRect)
        val bitmap: Bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)

        var shapeDrawable: ShapeDrawable

        // rectangle positions
        var left = 100
        var top = 100
        var right = 600
        var bottom = 400

        // draw rectangle shape to canvas
        shapeDrawable = ShapeDrawable(RectShape())
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.getPaint().setColor(Color.parseColor("#009944"))
        shapeDrawable.draw(canvas)

        // oval positions
        left = 100
        top = 500
        right = 600
        bottom = 800

        // draw oval shape to canvas
        shapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.getPaint().setColor(Color.parseColor("#009191"))
        shapeDrawable.draw(canvas)

        // now bitmap holds the updated pixels

        // set bitmap as background to ImageView
        imageView.background = BitmapDrawable(getResources(), bitmap)


        //test2

    }

    fun testRectangle2(){
        val rootLayout = findViewById<ViewGroup>(R.id.contDrag)
        val img = rootLayout.findViewById<ImageView>(R.id.imgTest)
        val layoutParams = RelativeLayout.LayoutParams(100,100)
        img.layoutParams = layoutParams
        val tlist = ChoiceTouchListener(rootLayout)
        img.setOnTouchListener(tlist)
        rootLayout.invalidate()
    }
}