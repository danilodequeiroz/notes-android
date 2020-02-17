package com.danilodequeiroz.notes_persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface LoginDataDao {

    /**
     * Get all Login data by id.
     * @return all Login data.
     */
    @Query("SELECT * FROM LoginData ORDER BY email")
    fun getAllLogins(): Flowable<MutableList<LoginData>>

    /**
     * Get a Login data by email.
     * @return the Login data from the table with a specific email.
     */
    @Query("SELECT * FROM LoginData WHERE email = :email")
    fun getLogin(email: String): Flowable<LoginData>

    /**
     * Insert a Login Data in the database. If the Login Data already exists, replace it.
     * @param data the Login Data to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(data: LoginData): Single<Long>

    /**
     * Delet a login data in the database.
     * @param email the Login Data id to be deleted.
     */
    @Query("DELETE FROM LoginData WHERE email = :email")
    fun deleteLogin(email: String)

}