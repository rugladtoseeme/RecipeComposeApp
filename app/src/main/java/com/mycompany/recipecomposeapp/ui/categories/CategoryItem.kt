package com.mycompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mycompany.recipecomposeapp.R
import com.mycompany.recipecomposeapp.data.model.CategoryUiModel

@Composable
fun CategoryItem(onClick: () -> Unit, categoryUiModel: CategoryUiModel) {
    Card(
        modifier = Modifier
            .height(height = 220.dp)
            .width(width = 156.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Column (verticalArrangement = Arrangement.SpaceBetween){
            AsyncImage(
                model = categoryUiModel.imageUrl,
                contentDescription = "recipe category image",
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_error),
                modifier = Modifier
                    .height(height = 130.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)){
                Text(
                    text = categoryUiModel.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = categoryUiModel.description, style = MaterialTheme.typography.bodySmall)

            }

        }
    }
}