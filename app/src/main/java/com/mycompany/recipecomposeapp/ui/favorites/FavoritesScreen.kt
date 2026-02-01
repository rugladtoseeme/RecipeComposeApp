package com.mycompany.recipecomposeapp.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.mycompany.recipecomposeapp.ui.core.ui.ScreenHeader

@Composable
fun FavoritesScreen(drawableResId: Int, headerText: String, modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            drawableResId,
            headerText
        )

        Spacer(Modifier.weight(weight = 1f))

        Text(
            text = "Избранные рецепты",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 30.sp
        )

        Spacer(Modifier.weight(weight = 1f))
    }
}