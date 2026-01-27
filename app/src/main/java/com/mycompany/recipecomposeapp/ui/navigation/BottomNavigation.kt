package com.mycompany.recipecomposeapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycompany.recipecomposeapp.R

@Composable
fun BottomNavigation(onCategoriesClick: () -> Unit, onFavoritesClick: () -> Unit) {

    Row(modifier = Modifier.navigationBarsPadding()) {
        Button(
            onClick = onCategoriesClick, Modifier
                .height(36.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Text(
                text = "КАТЕГОРИИ",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Spacer(Modifier.width(width = 4.dp))

        Button(
            onClick = onFavoritesClick,
            Modifier
                .height(36.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error, contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.error,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Row {
                Text(
                    text = "ИЗБРАННОЕ",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Image(
                    painter = painterResource(R.drawable.ic_heart_empty),
                    contentDescription = "favorites icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}

@Composable
@Preview
fun BottomNavigationPreview() {
    BottomNavigation({}, {})
}