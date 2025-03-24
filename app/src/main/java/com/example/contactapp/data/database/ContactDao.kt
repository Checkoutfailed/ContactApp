package com.example.contactapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsert(contact: ContactEntity)

    @Query("SELECT * FROM ContactEntity")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM ContactEntity WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM ContactEntity WHERE name LIKE '%' || :searchQuery || '%'")
    suspend fun searchContacts(searchQuery: String): List<ContactEntity>

    @Query("SELECT * FROM ContactEntity WHERE id = :id")
    suspend fun getContact(id: Int): ContactEntity?

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

}