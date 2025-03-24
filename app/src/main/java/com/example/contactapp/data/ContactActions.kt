package com.example.contactapp.data

import com.example.contactapp.data.database.ContactEntity


sealed interface ContactActions {
    data class OnSearchQueryChange(val query: String) : ContactActions
    data class OnContactSelected(val contact: ContactEntity?) : ContactActions
    data class OnDeleteContact(val contact: ContactEntity?) : ContactActions
    data class OnCreateToggle(val isOpen: Boolean) : ContactActions
    object SearchContacts: ContactActions
    object SaveContact: ContactActions
    data class SetName(val name: String) : ContactActions
    data class GetContact(val id: Int) : ContactActions
    data class SetPhoneNumbers(val phoneNumber: String) : ContactActions
    data class ImageUrl(val imageUrl: String) : ContactActions
    data class SetEmail(val email: String) : ContactActions
    data class SetOccupation(val occupation: String) : ContactActions
    data class SetNotes(val notes: String) : ContactActions
    data class SetIsFavorite(val isFavorite: Boolean) : ContactActions
    data class SetContactFilter(val filter: ContactFilter) : ContactActions
}