package com.mvvm.contactsapproom.ui.contact

import com.mvvm.contactsapproom.application.contact.ContactDto

interface ContactUiState {
    data class Success(val contacts: List<ContactDto>) : ContactUiState
    data class Error(val throwable: Throwable) : ContactUiState
    object Loading : ContactUiState
}