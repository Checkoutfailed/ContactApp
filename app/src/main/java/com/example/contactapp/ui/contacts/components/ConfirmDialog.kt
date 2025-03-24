package com.example.contactapp.ui.contacts.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.contactapp.R


@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if(showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(title)
            },
            text = {
                Text(text)
            },
            confirmButton = {
                Button (
                    onClick = onConfirm
                ) {
                    Text(stringResource(R.string.confirm_dialog))
                }
            },
            dismissButton = {
                Button (
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.cancel_dialog))
                }
            }
        )
    }
}