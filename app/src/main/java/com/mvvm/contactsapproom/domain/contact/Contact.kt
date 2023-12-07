package com.mvvm.contactsapproom.domain.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    val id:Int,
    val name:String,
    val lastname:String,
    val phoneNumber:String,
    val enterprise:String
)
