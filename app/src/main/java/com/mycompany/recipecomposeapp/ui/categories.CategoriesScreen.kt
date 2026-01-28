package com.mycompany.recipecomposeapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.R

@Composable
fun ScreenHeader(drawableResId: Int, headerText: String) {

    Box(
        modifier = Modifier
            .height(224.dp)
           .fillMaxSize()
    )
    {
        Image(
            painter = painterResource(drawableResId),
            contentDescription = "Screen header background picture.",
            modifier = Modifier
                .fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Surface(
            color = Color.White,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(start = 16.dp, top = 164.dp)
                .width(149.dp)
                .height(44.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
    }
}

@Composable
@Preview
fun CategoriesScreenPreview() {
    ScreenHeader(drawableResId = R.drawable.img_categories_header, headerText = "Заголовок")
}