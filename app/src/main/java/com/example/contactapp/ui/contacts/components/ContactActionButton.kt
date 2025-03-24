package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.ui.theme.SystemDarkGrey

@Composable
fun ContactActionButton (
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
) {
    Surface (
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .widthIn(max = 90.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
        ,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SystemDarkGrey)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    fontSize = 12.sp,
                    color = Color.White
                )
        }
    }
}