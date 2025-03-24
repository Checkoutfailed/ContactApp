package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.contactapp.ui.theme.HyperBlue


@Composable
fun ContactInfoButton (
    text: String,
    isWarning: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {

    Surface(
        modifier = Modifier
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        ,
        color = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray,
            ),
            shape = RoundedCornerShape(10.dp),
            enabled = isEnabled

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = text, color = if (isWarning) Color.Red else HyperBlue)
            }
        }
    }
}