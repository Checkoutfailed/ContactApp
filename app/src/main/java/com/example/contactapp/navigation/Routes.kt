package com.example.contactapp.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object ContactGraph: Route

    @Serializable
    data object Contacts: Route

    @Serializable
    data class Contact(val id: String): Route

    @Serializable
    data class EditContact(val id: String): Route
}