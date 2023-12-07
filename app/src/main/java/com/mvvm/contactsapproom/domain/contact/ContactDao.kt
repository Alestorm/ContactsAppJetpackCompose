package com.mvvm.contactsapproom.domain.contact

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ContactDao {
    @Query("select * from Contact")
    fun getContacts(): Flow<List<Contact>>

    @Query("select * from Contact where id = :id")
    suspend fun getContact(id:Int):Contact

    @Insert
    suspend fun addContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}