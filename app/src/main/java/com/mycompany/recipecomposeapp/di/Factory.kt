package com.mycompany.recipecomposeapp.di

interface Factory<T> {
    fun create(): T
}