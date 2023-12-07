package com.mvvm.contactsapproom.infraestructure.contact

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvvm.contactsapproom.domain.contact.Contact
import com.mvvm.contactsapproom.domain.contact.ContactDao

@Database(version = 1, entities = [Contact::class])
abstract class ContactDatabase :RoomDatabase(){
    abstract fun contactDao(): ContactDao
}