package com.mycompany.recipecomposeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mycompany.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    RecipeComposeAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Text(text = "Recipes App", modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
@Preview
fun RecipesAppPreview() {
    RecipesApp()
}