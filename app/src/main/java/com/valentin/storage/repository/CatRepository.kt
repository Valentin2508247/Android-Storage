package com.valentin.storage.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.valentin.storage.models.AppDatabase
import com.valentin.storage.models.Cat
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val database: AppDatabase,
) {

    private val states = mapOf<String, IDatabaseState>(
        "room" to RoomState(),
        "sqlite" to SQLiteState()
    )
    private var state = states["room"]!!

    fun useRoom() {
        Log.d(TAG, "Room state")
        state = states["room"]!!
    }

    fun useSQLite() {
        Log.d(TAG, "SQLite state")
        state = states["sqlite"]!!
    }

    fun close() {
        states["room"]?.close()
        states["sqlite"]?.close()
    }

    fun addCat(cat: Cat) {
        state.addCat(cat)
    }

    fun readCats(name: String?, age: Int?, breed: String?): LiveData<List<Cat>> {
        return state.readCats(name, age, breed)
    }

    fun readCat(id: Int): Cat?
    {
        return state.readCat(id)
    }

    fun updateCat(cat: Cat) {
        state.updateCat(cat)
    }

    fun deleteCat(cat: Cat) {
        state.deleteCat(cat)
    }

    sealed interface IDatabaseState {
        fun addCat(cat: Cat)
        fun readCats(name: String?, age: Int?, breed: String?): LiveData<List<Cat>>
        fun readCat(id: Int): Cat?
        fun updateCat(cat: Cat)
        fun deleteCat(cat: Cat)
        fun close()
    }

    inner class RoomState: IDatabaseState {
        override fun addCat(cat: Cat) {
            Log.d(TAG, "Room add")
            database.catDao().insertCat(cat)
        }

        override fun readCats(name: String?, age: Int?, breed: String?): LiveData<List<Cat>> {
            Log.d(TAG, "Room read cats")
            return database.catDao().query()
//            return if (age == null)
//                database.catDao().query(name?:"", breed?:"")
//            else
//                database.catDao().query(name?:"", age, breed?:"")
        }

        override fun updateCat(cat: Cat) {
            Log.d(TAG, "Room update cat")
            database.catDao().update(cat)
        }

        override fun deleteCat(cat: Cat) {
            Log.d(TAG, "Room delete cat")
            database.catDao().delete(cat)
        }

        override fun readCat(id: Int): Cat? {
            Log.d(TAG, "Room read cat")
            return database.catDao().readCat(id)
        }

        override fun close() {

        }
    }

    inner class SQLiteState: IDatabaseState {
        var data = MutableLiveData<List<Cat>>()
        private val db: SupportSQLiteDatabase by lazy {
            database.openHelper.writableDatabase
        }
//        private val db: SQLiteDatabase = try {
//            dbHelper.writableDatabase
//        }
//        catch (ex: SQLiteException) {
//            dbHelper.readableDatabase
//        }

        override fun addCat(cat: Cat) {
            Log.d(TAG, "SQLite add cat")
            val cv = ContentValues()
            cv.apply {
                put("age", cat.age)
                put("name", cat.name)
                put("breed", cat.breed)
            }
            val id = db.insert("Cat", OnConflictStrategy.REPLACE, cv)
            updateData()
        }

        private fun selectCats(): List<Cat> {
            val result = ArrayList<Cat>()
            //Log.d(TAG, "Start read sqlite")
            val cursor = db.query("Select * from cat")
            if (cursor.moveToFirst())
                do {
                    val cat = Cat(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        age = cursor.getInt(cursor.getColumnIndex("age")),
                        name = cursor.getString(cursor.getColumnIndex("name")),
                        breed = cursor.getString(cursor.getColumnIndex("breed"))
                    )
                    result.add(cat)
                } while (cursor.moveToNext())

            cursor.close()
            return result
        }

        override fun readCats(name: String?, age: Int?, breed: String?): LiveData<List<Cat>> {
            Log.d(TAG, "SQLite read cats")
            val catList = selectCats()
            data.value = catList
            return data
        }

        override fun updateCat(cat: Cat) {
            Log.d(TAG, "SQLite update cat")
            val query = "update Cat set age = ${cat.age}, name = '${cat.name}', breed = '${cat.breed}' " +
                    "where id = ${cat.id};"
            db.execSQL(query)
            updateData()
        }

        override fun deleteCat(cat: Cat) {
            Log.d(TAG, "SQLite delete")
            db.execSQL("delete from Cat where id = ${cat.id}")
            updateData()
        }

        private fun updateData() {
            val catList = selectCats()
            data.postValue(catList)
        }

        override fun readCat(id: Int): Cat? {
            Log.d(TAG, "SQLite read cat")
            var result: Cat? = null
            val cursor = db.query("Select * from cat where id = $id")
//            val cursor = db.query("Cat",
//                null,
//                "id=?",
//                arrayOf(id.toString()),
//                null,
//                null,
//                null)
            if (cursor.moveToFirst())
                do {
                    val cat = Cat(
                        id = cursor.getInt(cursor.getColumnIndex("id")),
                        age = cursor.getInt(cursor.getColumnIndex("age")),
                        name = cursor.getString(cursor.getColumnIndex("name")),
                        breed = cursor.getString(cursor.getColumnIndex("breed"))
                    )
                    result = cat
                } while (cursor.moveToNext())

            cursor.close()
            return result
        }

        override fun close() {
            db.close()
        }
    }

    private companion object {
        const val TAG = "CatRepository"
    }
}