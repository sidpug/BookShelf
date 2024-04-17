package com.sidpug.bookshelf.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sidpug.bookshelf.database.entity.User


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User> //maintain userid

    @Update
    suspend fun updateUsers(vararg users: User)

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE name LIKE :name  LIMIT 1")
    suspend fun findByName(name: String): User

    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)
}