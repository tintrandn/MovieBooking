package com.example.myapplication.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    val userName: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)