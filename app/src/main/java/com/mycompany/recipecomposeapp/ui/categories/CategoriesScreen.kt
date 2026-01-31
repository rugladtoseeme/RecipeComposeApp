package com.mycompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.ui.core.uui.ScreenHeader


@Composable
fun CategoriesScreen(drawableResId: Int, headerText: String, modifier: Modifier = Modifier) {

    Column(modifier = modifier.fillMaxWidth()) {
        ScreenHeader(
            drawableResId,
            headerText
        )

        Spacer(Modifier.weight(weight = 1f))

        Text(
            text = "Список категорий",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 30.sp
        )

        Spacer(Modifier.weight(weight = 1f))
    }
}

@Composable
@Preview
fun CategoriesScreenPreview() {
    ScreenHeader(imagePainter = R.drawable.img_categories_header, title = "Заголовок")
}