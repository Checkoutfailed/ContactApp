package com.example.contactapp.ui.contacts


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contactapp.R
import com.example.contactapp.data.ContactActions
import com.example.contactapp.data.ContactFilter
import com.example.contactapp.data.ContactViewModel
import com.example.contactapp.data.ContactsState
import com.example.contactapp.data.database.ContactEntity
import com.example.contactapp.ui.contacts.components.ContactImage
import com.example.contactapp.ui.contacts.components.ContactInfoButton
import com.example.contactapp.ui.contacts.components.ContactList
import com.example.contactapp.ui.contacts.components.ContactSearchBar
import com.example.contactapp.ui.contacts.components.DefaultContactProfile
import com.example.contactapp.ui.contacts.components.FormInputAlternative
import com.example.contactapp.ui.contacts.components.ImageGrabber
import com.example.contactapp.ui.contacts.components.SearchList
import com.example.contactapp.ui.theme.HyperBlue
import com.example.contactapp.ui.theme.SurfaceColorLight
import com.example.contactapp.ui.theme.SystemDarkGrey
import com.example.contactapp.ui.theme.SystemGrey
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts (
    viewModel: ContactViewModel,
    onContactSelected: (ContactEntity) -> Unit,
    onAction: (ContactActions) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    CreateContactForm(
        state = state,
        onAction = onAction,
        sheetState = sheetState,
        isOpen = state.isCreateOpen,
        onCreateToggle = { isOpen ->
            onAction(ContactActions.OnCreateToggle(isOpen))
        }
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .clickable(onClick = {
                        when(state.contactFilter) {
                            ContactFilter.ALL_CONTACTS -> {
                                onAction(ContactActions.SetContactFilter(ContactFilter.FAVORITES))
                                onAction(ContactActions.SetIsFavorite(true))
                            }
                            ContactFilter.FAVORITES -> {
                                onAction(ContactActions.SetContactFilter(ContactFilter.ALL_CONTACTS))
                                onAction(ContactActions.SetIsFavorite(false))
                            }
                        }
                    })
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = HyperBlue,
                )

                Text(
                    text = when(state.contactFilter) {
                        ContactFilter.ALL_CONTACTS -> stringResource(R.string.favorite_title)
                        ContactFilter.FAVORITES -> stringResource(R.string.all_contacts_title)
                    },
                    fontWeight = FontWeight.Bold,
                    color = HyperBlue
                )
            }

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = HyperBlue,
                modifier = Modifier
                    .clickable(onClick = {
                        onAction(ContactActions.OnCreateToggle(isOpen = true))
                    })
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = when(state.contactFilter) {
                    ContactFilter.ALL_CONTACTS -> stringResource(R.string.all_contacts_title)
                    ContactFilter.FAVORITES -> stringResource(R.string.favorite_title)
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        ContactSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(ContactActions.OnSearchQueryChange(it))
                debounceJob?.cancel()
                debounceJob = coroutineScope.launch {
                    delay(500L)
                    onAction(ContactActions.SearchContacts)
                }
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        )

        if(state.searchResults.isEmpty()) {

            if(state.contacts.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    color = SurfaceColorLight
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp,
                                start = 10.dp,
                                end = 0.dp
                            )
                    ) {

                        ContactList(
                            contacts = state.contacts,
                            onContactSelected = onContactSelected,
                        )
                    }

                }
            } else {
                Column (
                    modifier =  Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_contacts),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.contacts_you_create_will_appear_here),
                        fontSize = 18.sp,
                        color = SystemDarkGrey,
                    )
                }
            }

        } else {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                color = SystemGrey
            ){
                SearchList(
                    searchResults = state.searchResults,
                    onContactSelected = onContactSelected,
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateContactForm (
    state: ContactsState,
    onAction: (ContactActions) -> Unit,
    sheetState: SheetState,
    isOpen: Boolean,
    onCreateToggle : (Boolean) -> Unit,
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val defaultSheetHeight = screenHeight * 0.8f
    val submitColor = if(state.name.isBlank()) SystemGrey else HyperBlue
    val scope = rememberCoroutineScope()
    val scrollState: ScrollState = rememberScrollState()

    if(isOpen) {
        LaunchedEffect(Unit) {
            sheetState.expand()
        }
        ModalBottomSheet(
            onDismissRequest = {
                onCreateToggle(false)
                onAction(ContactActions.OnContactSelected(null))
            },
            shape = RoundedCornerShape(10.dp),
            containerColor = Color.White,
            sheetState = sheetState
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(defaultSheetHeight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Cancel",
                            color = HyperBlue,
                            modifier = Modifier
                                .clickable(onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onAction(ContactActions.OnContactSelected(null))
                                    }
                                })
                        )

                        Text(
                            text = "Add Contact",
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Text(
                            text = "Done",
                            color = submitColor
                            ,
                            modifier = Modifier
                                .clickable(onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onCreateToggle(false)
                                        onAction(ContactActions.SaveContact)
                                    }
                                })
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))


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
                        if (state.imageUrl.isEmpty()) {
                            DefaultContactProfile(
                                text = if (state.name.isNotEmpty()) state.name[0].uppercase() else "",
                            )
                        } else {
                            ContactImage(
                                imageUrl = state.imageUrl,
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(15.dp))


                    Column(
                        modifier = Modifier
                            .background(color = SystemGrey)
                            .fillMaxHeight()
                            .verticalScroll(scrollState),
                    ) {
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
                            text = if (state.isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(
                                R.string.add_to_favorites
                            ),
                            onClick = {
                                onAction(ContactActions.SetIsFavorite(!state.isFavorite))
                            }
                        )

                    }

                }


            }
        }

    }

}
