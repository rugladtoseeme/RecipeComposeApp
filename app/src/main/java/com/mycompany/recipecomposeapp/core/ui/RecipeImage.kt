package com.mycompany.recipecomposeapp.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.mycompany.recipecomposeapp.R

@Composable
fun RecipeImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val context = LocalContext.current
    AsyncImage(
        model = remember {ImageRequest.Builder(context)
            .data(imageUrl)
            .size(200, 130)
            .scale(Scale.FILL)
            .crossfade(durationMillis = 300)
            .build()},
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.img_placeholder),
        error = painterResource(R.drawable.img_error),
        modifier = modifier,
        contentScale = contentScale
    )
}