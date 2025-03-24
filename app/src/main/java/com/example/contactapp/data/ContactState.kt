package com.example.contactapp.data

import com.example.contactapp.data.database.ContactEntity


data class ContactsState(
    val searchQuery: String = "",
    val searchResults: List<ContactEntity> = emptyList(),
    val contacts: List<ContactEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isCreateOpen: Boolean = false,
    val errorMessage: String? = null,
    val selectedContactId: Int? = null,
    val selectedContact: ContactEntity? = null,
    val contactFilter: ContactFilter = ContactFilter.ALL_CONTACTS,

    val name: String = "",
    val phoneNumbers: String = "",
    val imageUrl: String = "",
    val email: String = "",
    val occupation: String = "",
    val notes: String = "",
    val isFavorite: Boolean = false,
)

enum class ContactFilter {
    ALL_CONTACTS,
    FAVORITES
}