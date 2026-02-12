package com.mycompany.recipecomposeapp.ui.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mycompany.recipecomposeapp.R

@Composable
fun ScreenHeader(imageUrl: String?, title: String) {
    ScreenHeaderImpl(imagePainter = imageUrl, title = title)
}

@Composable
fun ScreenHeader(imageResId: Int?, title: String) {
    ScreenHeaderImpl(imagePainter = imageResId, title = title)
}

@Composable
private fun ScreenHeaderImpl(imagePainter: Any?, title: String) {

    Box(
        modifier = Modifier
            .height(224.dp)
            .fillMaxSize()
    )
    {
        AsyncImage(
            model = imagePainter,
            contentDescription = "Screen header background picture",
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_error),
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            color = Color.White,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 102.dp,
                )
                .align(Alignment.BottomStart)
        ) {

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
    }
}