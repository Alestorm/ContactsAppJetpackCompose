package com.mvvm.contactsapproom.application.contact

data class ContactDto (
    val id:Int = System.currentTimeMillis().hashCode(),
    val name:String,
    val lastname:String,
    val phoneNumber:String,
    val enterprise:String
)