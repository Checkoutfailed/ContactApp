package com.example.contactapp.ui.contacts

import android.widget.Space
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp.R
import com.example.contactapp.data.ContactActions
import com.example.contactapp.data.database.ContactEntity
import com.example.contactapp.ui.contacts.components.ContactActionButton
import com.example.contactapp.ui.contacts.components.ContactImage
import com.example.contactapp.ui.contacts.components.ContactInfoBox
import com.example.contactapp.ui.contacts.components.ContactInfoButton
import com.example.contactapp.ui.contacts.components.DefaultContactProfile
import com.example.contactapp.ui.theme.HyperBlue
import com.example.contactapp.ui.theme.SurfaceColorLight
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey

@Composable
fun SelectedContact (
    contact: ContactEntity,
    onAction: (ContactActions) -> Unit,
    onBackClick: () -> Unit,
    onEditContact: () -> Unit,
) {

    val isFavoriteEnable = remember { mutableStateOf(contact.isFavorite) }
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SurfaceColorLight)
            .statusBarsPadding(),
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackClick() }
                    ,
                    tint = HyperBlue
                )
                Text(
                    text = "Edit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = HyperBlue,
                    modifier = Modifier
                        .clickable(onClick = onEditContact),

                    )
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .widthIn(max = 80.dp)
                        .heightIn(max = 80.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = SystemDarkGrey
                    ),
                    shape = CircleShape
                ) {
                    if(contact.imageUrl.isEmpty()) {
                        DefaultContactProfile(
                            text = contact.name[0].uppercase(),
                        )
                    } else {
                        ContactImage(
                            imageUrl = contact.imageUrl,
                        )
                    }
                }

                Spacer(modifier = Modifier.heightIn(10.dp))


                Text(
                    text = contact.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                )

                Spacer(modifier = Modifier.heightIn(10.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContactActionButton(
                        text = stringResource(R.string.call_button_action),
                        onClick = { /*TODO*/ },
                        icon = Icons.Default.Call
                    )
                    ContactActionButton(
                        text = stringResource(R.string.message_button_action),
                        onClick = { /*TODO*/ },
                        icon = Icons.Default.Send
                    )
                    ContactActionButton(
                        text = stringResource(R.string.mail_resource_action),
                        onClick = { /*TODO*/ },
                        icon = Icons.Default.Email
                    )
                }

            }


            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 10.dp)
                    .background(color = SystemGrey)
                    .verticalScroll(scrollState)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(contact.phoneNumbers.isNotBlank()) {
                    ContactInfoBox(
                        title = "Phone Number",
                        value = contact.phoneNumbers
                    )
                }

                if(contact.email.isNotBlank()) {
                    ContactInfoBox(
                        title = "Email",
                        value = contact.email
                    )
                }

                if(contact.occupation.isNotBlank()) {
                    ContactInfoBox(
                        title = "Address",
                        value = contact.occupation
                    )
                }

                if(contact.notes.isNotBlank()) {
                    ContactInfoBox(
                        title = "Notes",
                        value = contact.notes
                    )
                }

                ContactInfoButton(
                    text = if (isFavoriteEnable.value) stringResource(R.string.remove_from_favorites) else stringResource(
                        R.string.add_to_favorites
                    ),
                    onClick = {
                        onAction(ContactActions.SetIsFavorite(!contact.isFavorite))
                        isFavoriteEnable.value = !isFavoriteEnable.value
                    }
                )

                Spacer(modifier = Modifier.heightIn(50.dp))

            }

        }
    }

}