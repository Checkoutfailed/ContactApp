package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.data.database.ContactEntity
import com.example.contactapp.ui.theme.SystemGrey


@Composable
fun SearchListItem (
    contact: ContactEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface (
        modifier = modifier
            .clickable(onClick = onClick),

        color = Color.White,

        ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .drawBehind {

                    val strokeWidth = 0.5.sp.toPx() * density
                    val y = size.height - strokeWidth / 2

                    drawLine(
                        SystemGrey,
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )

                },
            verticalAlignment = Alignment.CenterVertically

        ){
            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = contact.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,

                    modifier = Modifier
                        .padding(10.dp)
                )
            }

        }
    }
}