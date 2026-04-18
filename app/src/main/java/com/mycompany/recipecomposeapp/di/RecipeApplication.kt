package com.mycompany.recipecomposeapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer
        private set


    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}