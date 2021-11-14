package com.example.networkbm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.*

import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.drawToBitmap
import androidx.drawerlayout.widget.DrawerLayout
import com.example.networkbm.fragments.AjoutObjetDialogFragment
import com.example.networkbm.fragments.ChangePlanFragment
import com.example.networkbm.fragments.EditConnexionDialogFragment
import com.example.networkbm.models.*
import com.example.networkbm.views.WScrollView
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), DeptListener {

    private lateinit var toggle: ActionBarDrawerToggle
    private var modeSelected : Mode = Mode.AUCUN
    private var isPressed = false
    private lateinit var objetAModifier : Objet
    private var drawGraph = DrawableGraph(this)
    var reseau = drawGraph.reseau
    private var connexionAModifier : Connexion? = null
    private lateinit var tableButsMenu : Array<ImageButton>
    private var savePosX = 0F
    private var savePosY = 0F
    private var saveScrollX : Int? = null
    private var saveScrollY : Int? = null
    private lateinit var ecran : ImageView
    private lateinit var dragOnTouch : TouchDragObject
    private var dragging = false
    private var saveReseau = SaveReseau()

    private lateinit var locale: Locale
    private var currentLanguage = "fr"
    private var currentLang: String? = null

    private var handler : Handler? = null

    private lateinit var affPrinc : TextView
    private lateinit var hsv : WScrollView

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
            findViewById(R.id.butAjoutObjet),
            findViewById(R.id.butAjoutConnexion),
            findViewById(R.id.butModif)
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
                R.id.button_changePlan -> affChangePlan()
                R.id.button_restore -> lireReseau()
                R.id.button_sendMail -> sendMail()
            }
            drawerLayout.closeDrawer(Gravity.LEFT)
            true
        }

        for(i in 0..tableButsMenu.size-1){
            tableButsMenu[i].setOnClickListener{
                clickMenu(i+1)
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein))
            }
        }
        val plan = findViewById<ImageView>(R.id.planAppartement)
        ecran = findViewById(R.id.contRect)

        ecran.setImageDrawable(drawGraph)

        // Bidirectionnal scrollview
        val sv = findViewById<ScrollView>(R.id.scrollView)
        hsv = findViewById(R.id.scrollViewH)
        hsv.sv = sv
        hsv.ecran = ecran
        dragOnTouch = TouchDragObject(hsv, reseau, plan)
        plan.doOnPreDraw {
            val param: FrameLayout.LayoutParams = FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,100)
            param.height = plan.height
            param.width = plan.width
            ecran.layoutParams = param
            ecran.invalidate()
        }

        handler = Handler(Looper.getMainLooper())

        this.verifyStoragePermissions()
        initListeners()
    }


    private fun reinitialisation()
    {
        reseau.connexions.clear()
        reseau.objets.clear()
        ecran.invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initListeners()
    {
        hsv.setOnTouchListener { _, event ->
            ecran.dispatchTouchEvent(event)

            true
        }

        ecran.setOnLongClickListener {
            true
        }

        ecran.setOnTouchListener { _, event ->
            this.savePosX = event.x + hsv.scrollX
            this.savePosY = event.y + hsv.sv.scrollY
            val draggingObj = dragOnTouch.onTouch(null, event, savePosX, savePosY)
            val draggingLine = dragOnTouch.dragLine(
                null,
                event,
                savePosX,
                savePosY,
            )
            var draggingCourbure = false
            if(!draggingLine)
            {
                draggingCourbure = dragOnTouch.dragCourbure(
                    null,
                    event,
                    savePosX,
                    savePosY
                )
            }
            dragging = draggingLine || draggingObj || draggingCourbure
            when(event.action)
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
                            //On a recupere une connexion en mode MODIFICATION
                            val dialog = EditConnexionDialogFragment(connexion, reseau)
                            dialog.show(supportFragmentManager, resources.getString(R.string.editConnection))
                        } else if (connexion != null && modeSelected != Mode.MODIFICATION) {
                            System.out.println("HEY")
                            dragging = dragOnTouch.dragCourbure(connexion, event, savePosX, savePosY)
                        } else if(objet != null) {
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
                                        dragging = dragOnTouch.dragLine(
                                            connexionAModifier!!,
                                            event,
                                            savePosX,
                                            savePosY,
                                        )
                                    }
                                    else if(connexionAModifier != null)
                                    {
                                        dragging = dragOnTouch.dragLine(
                                            connexionAModifier!!,
                                            event,
                                            savePosX,
                                            savePosY,
                                        )
                                    }
                                }
                                Mode.MODIFICATION -> {
                                    //On met en mode modification, on affiche la fenetre de modification
                                    objetAModifier = objet
                                    val dialog = AjoutObjetDialogFragment(objet, reseau)
                                    dialog.show(supportFragmentManager, resources.getString(R.string.editObject))
                                }
                                else -> {

                                }
                            }
                        }
                        else if (modeSelected == Mode.AJOUT_CONNEXION && connexionAModifier != null)
                        {
                            dragging = dragOnTouch.dragLine(
                                connexionAModifier!!,
                                event,
                                savePosX,
                                savePosY,
                            )
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
                        if(objet != null && objet != connexionAModifier!!.getObjet1() && !reseau.existeConnexion(connexionAModifier!!.getObjet1(), objet))
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
                hsv.onTouchEvent(event)
            }

            ecran.invalidate()

            true
        }
        this.lireReseau()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clickMenu(i: Int)
    {
        val navView = findViewById<NavigationView>(R.id.navView)
        for(it in 0..tableButsMenu.size-1){
            tableButsMenu[it].setBackgroundColor(resources.getColor(R.color.white))
            navView.menu.getItem(it + 1).isChecked = false
        }
        navView.menu.getItem(i).isChecked = true
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
            tableButsMenu[i - 1].setBackgroundColor(resources.getColor(R.color.purple_sec))
        else
        {
            tableButsMenu[i - 1].setBackgroundColor(resources.getColor(R.color.white))
            navView.menu.getItem(i).isChecked = false
        }

        ecran.invalidate()
    }


    override fun onDeptSelected(depts: ArrayList<String>) {
        //Click sur une page de dialogue
        if(depts.size >= 1 && depts[0] == "changePlan"){
            //Changement de plan
            val img = depts[1]

            this.reseau.imgAppart = img
            this.loadImgAppart()
        } else {
            when (modeSelected) {
                Mode.AJOUT_OBJET -> {
                    objetAModifier.nom = depts[0]
                    objetAModifier.couleur = depts[1]
                    if (depts.size > 2) {
                        objetAModifier.icone = depts[2]
                    }
                    reseau.objets.add(objetAModifier)
                    Toast.makeText(this, getString(R.string.objectCreated), LENGTH_SHORT).show()
                }
                Mode.MODIFICATION -> {
                    val obj = reseau.getObjet(this.savePosX, this.savePosY)
                    if (obj != null) {
                        obj.nom = depts[0]
                        obj.couleur = depts[1]
                        if (depts.size > 2) {
                            obj.icone = depts[2]
                        } else {
                            obj.icone = null
                        }
                        Toast.makeText(this, getString(R.string.objectModified), LENGTH_SHORT)
                            .show()
                    }
                }
                else -> {

                }
            }
        }
        ecran.invalidate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("objets", reseau.objets)
        outState.putString("imgAppart", this.reseau.imgAppart)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        reseau.imgAppart = savedInstanceState.getString("imgAppart") as String
        reseau.objets = savedInstanceState.getParcelableArrayList<Objet>("objets") as ArrayList<Objet>
        reseau.objets.forEach { obj ->
            obj.connexions.forEach{
                if(!reseau.existeConnexion(it.getObjet1(), it.getObjet2()!!))
                {
                    reseau.connexions.add(it)
                }
            }
        }
        this.loadImgAppart()
        ecran.invalidate()
    }

    private fun setLocale(localeName: String) {
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
                this@MainActivity, "Language, , already, , selected)!", LENGTH_SHORT).show()
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

    private fun changeLanguage(){
        if(this.currentLanguage == "fr"){
            this.setLocale("en")
        } else {
            this.setLocale("fr")
        }
    }

    private fun saveReseau(){
        this.saveReseau.create(this, this.reseau)
        Toast.makeText(this, getString(R.string.networkSaved), LENGTH_SHORT).show()
    }

    private fun lireReseau() {
        this.reseau.objets.clear()
        this.reseau.connexions.clear()
        val isLoad = this.saveReseau.read(this, this.reseau)
        this.loadImgAppart()
        ecran.invalidate()
        if(!isLoad){
            this.affChangePlan()
        }
    }

    private fun affChangePlan() {
        val dialog = ChangePlanFragment()
        dialog.show(supportFragmentManager, resources.getString(R.string.changePlan))
    }

    private fun loadImgAppart() {
        val plan = findViewById<ImageView>(R.id.planAppartement)

        val path = this.resources.getIdentifier(this.reseau.imgAppart, "drawable", this.packageName)
        val nouvImg = BitmapFactory.decodeResource(this.resources, path)
        if(nouvImg != null) {
            plan.setImageBitmap(nouvImg)

            this.reseau.objets.forEach{
                if(nouvImg.width < it.centerX() - 50){
                    it.setPositions(nouvImg.width.toFloat() - 50, it.centerY())
                }
                if(nouvImg.height - 50 < it.centerY()){
                    it.setPositions(it.centerX(), nouvImg.height.toFloat() - 50)
                }
            }

            plan.doOnPreDraw {
                val param: FrameLayout.LayoutParams = FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,100)
                param.height = plan.height
                param.width = plan.width
                ecran.layoutParams = param
                ecran.invalidate()
            }
        }
    }

    private fun sendMail() {
        val subject = "Image réseau"
        val message = "Ceci est un message"

        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
        email.putExtra(Intent.EXTRA_TEXT, message)

        val bm = this.ecran.drawToBitmap()
        val bgbm = findViewById<ImageView>(R.id.planAppartement).drawToBitmap()
        val mergedImages = createSingleImageFromMultipleImages(bgbm, bm);

        val mFile = savebitmap(mergedImages);
        val u = Uri.fromFile(mFile)
        email.putExtra(Intent.EXTRA_STREAM, u);

        email.setType("message/rfc822")

        startActivity(Intent.createChooser(email, "Choose an Email client :"))
    }

    private fun createSingleImageFromMultipleImages(firstImage : Bitmap, secondImage : Bitmap) : Bitmap{
        val result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        val canvas = Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 0f, 0f, null);
        return result;
    }

    private fun savebitmap(bmp : Bitmap) : File? {
        val builder = StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        val extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        val temp = "screen"
        var file = File(extStorageDirectory, temp + ".png");
        System.out.println(file)

        val outStream = FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();

        return file;
    }

    private fun verifyStoragePermissions() {
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            val REQUEST_EXTERNAL_STORAGE = 1;
            val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}