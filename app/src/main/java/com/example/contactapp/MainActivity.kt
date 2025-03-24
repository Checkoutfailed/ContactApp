package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.contactapp.data.ContactActions
import com.example.contactapp.data.ContactViewModel
import com.example.contactapp.data.database.ContactDatabase
import com.example.contactapp.ui.theme.ContactAppTheme
import com.example.contactapp.navigation.Route
import com.example.contactapp.ui.contacts.Contacts
import com.example.contactapp.ui.contacts.EditContact
import com.example.contactapp.ui.contacts.SelectedContact

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contacts.db"
        ).build()
    }

    private val viewModel by viewModels<ContactViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.ContactGraph
                ) {
                    navigation<Route.ContactGraph> (
                        startDestination = Route.Contacts
                    ) {
                        composable<Route.Contacts> {

                            LaunchedEffect(true) {
                                viewModel.onAction(ContactActions.OnContactSelected(null))
                            }

                            Contacts(
                                viewModel = viewModel,
                                onContactSelected = {
                                    viewModel.onAction(ContactActions.OnContactSelected(it))
                                    navController.navigate(Route.Contact(it.id.toString()))
                                },
                                onAction = {
                                    viewModel.onAction(it)
                                }
                            )
                        }

                        composable<Route.Contact> {

                            LaunchedEffect(true) {
                                val id = it.arguments?.getString("id")
                                viewModel.onAction(ContactActions.GetContact(id!!.toInt()))
                            }

                            viewModel.state.value.selectedContact?.let { it1 ->
                                SelectedContact(
                                    contact = it1,
                                    onAction = {
                                        viewModel.onAction(it)
                                    },
                                    onBackClick = {
                                        navController.navigateUp()
                                    },
                                    onEditContact = {
                                        navController.navigate(Route.EditContact(it.id))
                                    }
                                )
                            }
                        }

                        composable<Route.EditContact> {
                            EditContact(
                                viewModel = viewModel,
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onConfirmEdit = {
                                    navController.navigateUp()
                                },
                                onConfirmDelete = {
                                    viewModel.onAction(ContactActions.OnDeleteContact(it))
                                    navController.navigate(Route.Contacts)
                                },
                                onAction = {
                                    viewModel.onAction(it)

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

