package com.example.networkbm

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.*

class LocaleHelper {

    var SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun setLocale(context: Context, language: String) : Context{
        this.persist(context, language)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return updateResources(context, language)
        }

        return updateResourcesLegacy(context, language)
    }

    fun persist(context: Context, language: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit() as SharedPreferences.Editor
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun updateResources(context: Context, language: String) : Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.getResources().getConfiguration() as Configuration;
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    fun updateResourcesLegacy(context: Context, language: String) : Context {
        val locale = Locale(language);
        Locale.setDefault(locale);

        val resources = context.getResources() as Resources;

        val configuration = resources.getConfiguration() as Configuration;
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}