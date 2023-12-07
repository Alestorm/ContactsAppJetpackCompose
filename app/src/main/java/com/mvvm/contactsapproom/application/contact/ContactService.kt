package com.mvvm.contactsapproom.application.contact

import com.mvvm.contactsapproom.infraestructure.contact.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ContactService @Inject constructor(
    private val contactRepository: ContactRepository
) {
    fun getContacts(): Flow<List<ContactDto>> {
        return contactRepository.contacts
    }

    suspend fun getContact(id: Int): ContactDto {
        return contactRepository.getContact(id)
    }

    suspend fun addContact(contactDto: ContactDto) {
        contactRepository.addContact(contactDto)
    }

    suspend fun updateContact(contactDto: ContactDto) {
        contactRepository.updateContact(contactDto)
    }

    suspend fun deleteContact(contactDto: ContactDto) {
        contactRepository.deleteContact(contactDto)
    }
}