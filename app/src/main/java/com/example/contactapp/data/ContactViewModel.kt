package com.example.contactapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.database.ContactDao
import com.example.contactapp.data.database.ContactEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(
    private val dao: ContactDao
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsState())
    private val _contactFilter = MutableStateFlow(ContactFilter.ALL_CONTACTS)

    private val _contacts = _contactFilter
        .flatMapLatest { filterType ->
            when (filterType) {
                ContactFilter.ALL_CONTACTS -> dao.getContacts()
                ContactFilter.FAVORITES -> dao.getFavorites()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _contacts, _contactFilter) { state, contacts, contactFilter ->
        state.copy(
            contacts = contacts,
            contactFilter = contactFilter
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactsState())

    fun onAction(action: ContactActions) {
        when (action) {
            is ContactActions.OnSearchQueryChange -> handleSearchQueryChange(action.query)
            is ContactActions.OnContactSelected -> handleContactSelection(action.contact)
            is ContactActions.OnCreateToggle -> updateState { it.copy(isCreateOpen = action.isOpen) }
            is ContactActions.SaveContact -> saveContact()
            is ContactActions.OnDeleteContact -> deleteContact(action.contact)
            is ContactActions.SetName -> updateState { it.copy(name = action.name) }
            is ContactActions.SetPhoneNumbers -> updateState { it.copy(phoneNumbers = action.phoneNumber) }
            is ContactActions.SearchContacts -> searchContacts()
            is ContactActions.GetContact -> fetchContact(action.id)
            is ContactActions.ImageUrl -> updateState { it.copy(imageUrl = action.imageUrl) }
            is ContactActions.SetEmail -> updateState { it.copy(email = action.email) }
            is ContactActions.SetOccupation -> updateState { it.copy(occupation = action.occupation) }
            is ContactActions.SetNotes -> updateContactNotes(action.notes)
            is ContactActions.SetIsFavorite -> updateFavoriteStatus(action.isFavorite)
            is ContactActions.SetContactFilter -> _contactFilter.value = action.filter
        }
    }

    private fun updateState(update: (ContactsState) -> ContactsState) {
        _state.update(update)
    }

    private fun handleSearchQueryChange(query: String) {
        updateState {
            it.copy(
                searchQuery = query,
                searchResults = if (query.isBlank()) emptyList() else it.searchResults)
        }
    }

    private fun handleContactSelection(contact: ContactEntity?) {
        updateState {
            it.copy(
                selectedContact = contact,
                name = contact?.name.orEmpty(),
                phoneNumbers = contact?.phoneNumbers.orEmpty(),
                imageUrl = contact?.imageUrl.orEmpty(),
                email = contact?.email.orEmpty(),
                occupation = contact?.occupation.orEmpty(),
                notes = contact?.notes.orEmpty(),
                isFavorite = contact?.isFavorite ?: false,
                isCreateOpen = contact?.isFavorite ?: false
            )
        }
    }

    private fun saveContact() {
        val stateValue = _state.value
        if (stateValue.name.isBlank()) return

        val contactEntity = ContactEntity(
            id = stateValue.selectedContact?.id,
            name = stateValue.name,
            phoneNumbers = stateValue.phoneNumbers,
            imageUrl = stateValue.imageUrl,
            email = stateValue.email,
            occupation = stateValue.occupation,
            notes = stateValue.notes,
            isFavorite = stateValue.isFavorite
        )

        viewModelScope.launch {
            dao.upsert(contactEntity)
            if (stateValue.selectedContact == null) resetForm()
        }
    }

    private fun resetForm() {
        updateState {
            it.copy(
                name = "",
                phoneNumbers = "",
                imageUrl = "",
                email = "",
                occupation = "",
                notes = "",
                isFavorite = false,
                isCreateOpen = false
            )
        }
    }

    // Delete Contact
    private fun deleteContact(contact: ContactEntity?) {
        contact?.let {
            viewModelScope.launch { dao.deleteContact(it) }
        }
    }

    // Search Contacts
    private fun searchContacts() {
        if (_state.value.searchQuery.isNotBlank()) {
            viewModelScope.launch {
                val searchResults = dao.searchContacts(_state.value.searchQuery)
                updateState { it.copy(searchResults = searchResults) }
            }
        }
    }

    private fun fetchContact(id: Int) {
        viewModelScope.launch {
            dao.getContact(id)?.let { contact ->
                updateState {
                    it.copy(
                        selectedContact = contact,
                        name = contact.name,
                        phoneNumbers = contact.phoneNumbers,
                        imageUrl = contact.imageUrl,
                        email = contact.email,
                        occupation = contact.occupation,
                        notes = contact.notes,
                        isFavorite = contact.isFavorite,
                        isCreateOpen = contact.isFavorite
                    )
                }
            }
        }
    }

    private fun updateContactNotes(notes: String) {
        val selectedContact = _state.value.selectedContact
        if (selectedContact != null) {
            val updatedContact = selectedContact.copy(notes = notes)
            viewModelScope.launch { dao.upsert(updatedContact) }
        }
        updateState { it.copy(notes = notes) }
    }

    private fun updateFavoriteStatus(isFavorite: Boolean) {
        val selectedContact = _state.value.selectedContact
        if (selectedContact != null) {
            val updatedContact = selectedContact.copy(isFavorite = isFavorite)
            viewModelScope.launch { dao.upsert(updatedContact) }
        }
        updateState { it.copy(isFavorite = isFavorite) }
    }
}
