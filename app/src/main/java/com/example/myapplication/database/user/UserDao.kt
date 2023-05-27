package com.example.myapplication.database.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userName = :userName AND password = :password")
    fun getUser(userName: String, password: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: UserEntity)

}