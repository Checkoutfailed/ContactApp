package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.R
import java.io.File

@Composable
fun ContactImage (
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val file = File(imageUrl)

    Image(
        painter = rememberAsyncImagePainter(
            model = file
        ),
        contentDescription = stringResource(R.string.selected_image),
        modifier = modifier
            .size(150.dp)
            .clip(CircleShape)
    )
}
