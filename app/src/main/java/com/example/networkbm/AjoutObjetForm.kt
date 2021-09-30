package com.example.networkbm

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText

class AjoutObjetForm (context: Context){

    var dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(context)
    lateinit var alertDialog : AlertDialog
    lateinit var editText : EditText
    lateinit var button: Button

}