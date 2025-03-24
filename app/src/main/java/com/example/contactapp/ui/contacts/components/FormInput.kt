package com.example.contactapp.ui.contacts.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey


@Composable
fun FormInput(
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        singleLine = singleLine,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = SystemDarkGrey,
            focusedContainerColor = SystemGrey,
            unfocusedContainerColor = SystemGrey,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(text = placeHolder)
        },
        modifier = Modifier
            .fillMaxWidth(),
    )
}

@Composable
fun FormInputAlternative(
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
) {

    Surface(
        modifier = Modifier
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        ,
        color = Color.White,
        shape = RoundedCornerShape(10.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
            singleLine = singleLine,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(text = placeHolder)
            },
            modifier = Modifier
                .fillMaxWidth(),
        )
    }

}