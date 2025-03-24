package com.example.contactapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ContactEntity::class],
    version = 1
)

abstract class ContactDatabase: RoomDatabase() {
    abstract val dao: ContactDao

}