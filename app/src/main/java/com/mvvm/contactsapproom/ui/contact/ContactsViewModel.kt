package com.mvvm.contactsapproom.ui.contact

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.contactsapproom.application.contact.ContactDto
import com.mvvm.contactsapproom.application.contact.ContactService
import com.mvvm.contactsapproom.domain.contact.Contact
import com.mvvm.contactsapproom.ui.contact.ContactUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactService: ContactService
) : ViewModel() {
    val uiState: StateFlow<ContactUiState> = contactService.getContacts().map(::Success)
        .catch { ContactUiState.Error(it) }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading
        )

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _showEditDialog = MutableLiveData<Boolean>()
    val showEditDialog: LiveData<Boolean> = _showEditDialog


    fun openEditDialog() {
        _showEditDialog.value = true
    }

    fun findContact(contactDto: ContactDto): ContactDto {
        var contact = contactDto
        viewModelScope.launch {
            contact = contactService.getContact(contactDto.id)
        }
        Log.i("nameContact",contact.name)
        return contact
    }

    fun closeEditDialog() {
        _showEditDialog.value = false
    }

    fun openDialog() {
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun addContact(contactDto: ContactDto) {
        closeDialog()
        viewModelScope.launch {
            contactService.addContact(contactDto)
        }
    }

    fun updateContact(contactDto: ContactDto) {
        closeDialog()
        viewModelScope.launch {
            contactService.updateContact(
                contactDto.copy(
                    id = contactDto.id,
                    name = contactDto.name,
                    lastname = contactDto.lastname,
                    phoneNumber = contactDto.phoneNumber,
                    enterprise = contactDto.enterprise
                )
            )
        }
    }

    fun deleteContact(contactDto: ContactDto) {
        viewModelScope.launch {
            contactService.deleteContact(contactDto)
        }
    }
}
