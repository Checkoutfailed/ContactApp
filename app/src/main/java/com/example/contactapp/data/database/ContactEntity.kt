package com.example.contactapp.data.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val phoneNumbers: String,
    val imageUrl: String,
    val email: String,
    val occupation: String,
    val notes: String,
    val isFavorite: Boolean,
)
