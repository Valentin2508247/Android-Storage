package com.valentin.storage.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCat(cat: Cat)

    @Query("Select * from cat order by id")
    fun query(
//        name: String, age: Int, breed: String
    ): LiveData<List<Cat>>

    @Query("Select * from cat where id = :id")
    fun readCat(id: Int): Cat?

    @Update
    fun update(cat: Cat)

    @Delete
    fun delete(cat: Cat)
}