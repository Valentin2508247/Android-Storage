package com.valentin.storage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat")
data class Cat(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var age: Int,
    var name: String,
    var breed: String
)