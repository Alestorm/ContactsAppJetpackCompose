package com.mvvm.contactsapproom.di

import android.content.Context
import androidx.room.Room
import com.mvvm.contactsapproom.domain.contact.ContactDao
import com.mvvm.contactsapproom.infraestructure.contact.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext context: Context): ContactDatabase {
        return Room.databaseBuilder(context, ContactDatabase::class.java, "ContactDatabase").build()
    }

    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.contactDao()
    }
}