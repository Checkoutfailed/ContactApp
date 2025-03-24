package com.example.contactapp.ui.contacts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contactapp.ui.theme.ContactAppTheme
import com.example.contactapp.R
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey

@Composable
fun ContactSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = SystemDarkGrey,
            focusedContainerColor = SystemGrey,
            unfocusedContainerColor = SystemGrey,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        placeholder = ({ Text(stringResource(R.string.search_contacts)) }),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = SystemDarkGrey
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty()
            ) {
                IconButton(
                    onClick = {
                        onSearchQueryChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_hint),
                        tint = SystemDarkGrey
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .background (
                shape = RoundedCornerShape(100),
                color = Color.White
            )
    )
}


@Preview(showBackground = true)
@Composable
fun ContactSearchBarPreview() {
    ContactAppTheme  {
        ContactSearchBar(
            searchQuery = "",
            onSearchQueryChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}