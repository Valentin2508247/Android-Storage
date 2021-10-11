package com.valentin.storage.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCat(cat: Cat)

    @Query("Select * from cat")
    fun query(): LiveData<List<Cat>>

    @Query("Select * from cat where name like :name and breed like :breed and age = :age order by name")
    fun query(
        name: String, age: Int, breed: String
    ): LiveData<List<Cat>>

    @Query("Select * from cat where name like :name and breed like :breed order by name")
    fun query(
        name: String, breed: String
    ): LiveData<List<Cat>>

//    @Query("Select * from cat where breed like :breed and age = :age order by name")
//    fun query(
//        age: Int, breed: String
//    ): LiveData<List<Cat>>
//
//    @Query("Select * from cat where name like :name and age = :age order by name")
//    fun query(
//        name: String, age: Int
//    ): LiveData<List<Cat>>
//
//    @Query("Select * from cat where name like :name order by name")
//    fun query(
//        name: String
//    ): LiveData<List<Cat>>
//
//    @Query("Select * from cat where age = :age order by name")
//    fun query(
//        age: Int
//    ): LiveData<List<Cat>>
//
//    @Query("Select * from cat where breed like :breed order by name")
//    fun queryBreed(
//        breed: String
//    ): LiveData<List<Cat>>

    @Query("Select * from cat where id = :id")
    fun readCat(id: Int): Cat?

    @Update
    fun update(cat: Cat)

    @Delete
    fun delete(cat: Cat)
}