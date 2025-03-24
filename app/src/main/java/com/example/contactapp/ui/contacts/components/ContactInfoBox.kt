package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ContactInfoBox (
    value: String,
    title: String? = null
) {
    Surface(
        modifier = Modifier
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        ,
        color = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
            ,
            verticalArrangement = Arrangement.Center
        ){
            title?.let { text ->
                Text(
                    text = text,
                    modifier = Modifier,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
