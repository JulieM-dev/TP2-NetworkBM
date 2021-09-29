package com.example.networkbm.models

import android.view.View
import androidx.constraintlayout.solver.widgets.Rectangle

class Objet {
    private lateinit var rect : View


    fun getView() : View
    {
        return rect
    }
}