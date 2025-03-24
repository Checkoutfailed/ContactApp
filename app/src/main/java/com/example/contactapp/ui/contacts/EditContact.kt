package com.example.contactapp.ui.contacts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contactapp.R
import com.example.contactapp.data.ContactActions
import com.example.contactapp.data.ContactViewModel
import com.example.contactapp.data.database.ContactEntity
import com.example.contactapp.ui.contacts.components.ConfirmDialog
import com.example.contactapp.ui.contacts.components.ContactImage
import com.example.contactapp.ui.contacts.components.ContactInfoButton
import com.example.contactapp.ui.contacts.components.DefaultContactProfile
import com.example.contactapp.ui.contacts.components.FormInputAlternative
import com.example.contactapp.ui.contacts.components.ImageGrabber
import com.example.contactapp.ui.theme.HyperBlue
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey

@Composable
fun EditContact (
    viewModel: ContactViewModel,
    onBackClick: () -> Unit,
    onConfirmEdit: () -> Unit,
    onConfirmDelete: (ContactEntity) -> Unit,
    onAction: (ContactActions) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState:ScrollState = rememberScrollState()
    val alertPopup = remember { mutableStateOf(false) }

    ConfirmDialog (
        title = "Delete Contact",
        text = "Are you sure you want to delete this contact?",
        showDialog = alertPopup.value,
        onDismiss = { alertPopup.value = false },
        onConfirm = {
            alertPopup.value = false
            state.selectedContact?.let { onConfirmDelete(it) }
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding(),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackClick() },
                    tint = HyperBlue
                )
                Text(
                    text = "Edit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = HyperBlue,
                    modifier = Modifier
                        .clickable(onClick = {
                            onAction(ContactActions.SaveContact)
                            onConfirmEdit()
                        }),

                    )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .widthIn(max = 80.dp)
                        .heightIn(max = 80.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = SystemDarkGrey
                    ),
                    shape = CircleShape
                ) {
                    if(state.imageUrl.isEmpty()) {
                        DefaultContactProfile(
                            text = if (state.name.isNotEmpty()) state.name[0].uppercase() else "",
                        )
                    } else {
                        ContactImage(
                            imageUrl = state.imageUrl,
                        )
                    }

                }

            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = SystemGrey)
                    .verticalScroll(scrollState)
                ,
            ){
                ImageGrabber(
                    onImageSelected = {
                        it?.let {
                            onAction(ContactActions.ImageUrl(it))
                        }
                    }
                )
                FormInputAlternative(
                    value = state.name,
                    onValueChange = {
                        onAction(ContactActions.SetName(it))
                    },
                    placeHolder = "Name"
                )

                FormInputAlternative(
                    value = state.phoneNumbers,
                    onValueChange = {
                        onAction(ContactActions.SetPhoneNumbers(it))
                    },
                    placeHolder = "Phone Number"
                )

                FormInputAlternative(
                    value = state.email,
                    onValueChange = {
                        onAction(ContactActions.SetEmail(it))
                    },
                    placeHolder = "Email"
                )

                FormInputAlternative(
                    value = state.occupation,
                    onValueChange = {
                        onAction(ContactActions.SetOccupation(it))
                    },
                    placeHolder = "Occupation"
                )

                FormInputAlternative(
                    value = state.notes,
                    onValueChange = {
                        onAction(ContactActions.SetNotes(it))
                    },
                    singleLine = false,
                    placeHolder = "Notes"
                )

                ContactInfoButton(
                    text = if(state.isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(
                        R.string.add_to_favorites
                    ) ,
                    onClick = {
                        onAction(ContactActions.SetIsFavorite(!state.isFavorite))
                    }
                )

                ContactInfoButton(
                    text = "Delete Contact",
                    isWarning = true,
                    onClick = {
                        alertPopup.value = true
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

            }


        }
    }

}

