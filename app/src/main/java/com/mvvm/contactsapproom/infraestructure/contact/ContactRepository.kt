package com.mvvm.contactsapproom.infraestructure.contact

import com.mvvm.contactsapproom.application.contact.ContactDto
import com.mvvm.contactsapproom.domain.contact.Contact
import com.mvvm.contactsapproom.domain.contact.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {
    val contacts: Flow<List<ContactDto>> = contactDao.getContacts()
        .map { items ->
            items.map {
                ContactDto(it.id, it.name, it.lastname, it.phoneNumber, it.enterprise)
            }
        }

    suspend fun getContact(id: Int): ContactDto {
        val contact = contactDao.getContact(id)
        return ContactDto(
            id = contact.id,
            name = contact.name,
            lastname = contact.lastname,
            phoneNumber = contact.phoneNumber,
            enterprise = contact.enterprise
        )
    }

    suspend fun addContact(contactDto: ContactDto) {
        contactDao.addContact(contactDto.toEntity())
    }

    suspend fun updateContact(contactDto: ContactDto) {
        contactDao.updateContact(contactDto.toEntity())
    }

    suspend fun deleteContact(contactDto: ContactDto) {
        contactDao.deleteContact(contactDto.toEntity())
    }


}

fun ContactDto.toEntity(): Contact {
    return Contact(
        this.id,
        this.name,
        this.lastname,
        this.phoneNumber,
        this.enterprise
    )
}