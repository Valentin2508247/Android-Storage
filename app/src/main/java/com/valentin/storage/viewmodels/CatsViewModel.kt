package com.valentin.storage.viewmodels

import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.valentin.storage.models.AppDatabase
import com.valentin.storage.models.Cat
import com.valentin.storage.models.DatabaseOpenHelper
import com.valentin.storage.repository.CatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatsViewModel(
    private val repository: CatRepository,
    private val preferences: SharedPreferences
    ): ViewModel() {

    var name: String? = ""
    var age: Int? = -1
    var breed: String? = ""

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener {
            pref, key ->
        Log.d(TAG, "key: $key")
        when (key) {
            "room" -> {
                val value = pref.getBoolean(key, false)
                if (value)
                    repository.useRoom()
                else
                    repository.useSQLite()
                Log.d(TAG, "Room pref: $value")
            }
            "name" -> {
                val value = pref.getString(key, "")
                name = value
                Log.d(TAG, "Name pref: $value")
            }
            "age" -> {
                val value = pref.getString(key, "")
                age = value?.toInt()
                Log.d(TAG, "Age pref: $value")
            }
            "breed" -> {
                val value = pref.getString(key, "")
                breed = value
                Log.d(TAG, "Breed pref: $value")
            }
        }
    }

    init {
        Log.d(TAG, "viewmodel init")
        if (preferences.getBoolean("room", true))
            repository.useRoom()
        else
            repository.useSQLite()
        name = preferences.getString("name", "")
        age = preferences.getString("age", "-1")?.toInt()
        breed = preferences.getString("breed", "")
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    val cats: LiveData<List<Cat>>
        get() = repository.readCats()
//    by lazy {
//        val liveData = repository.readCats()
//        liveData.ma
//
//        liveData
//    }
//    init {
//        viewModelScope.launch {
//            cats = repository.readCats().
//        }
//    }



    fun addCat(cat: Cat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCat(cat)
        }
    }

    fun updateCat(cat: Cat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCat(cat)
        }
    }

    fun readCat(id: Int): Cat? {
        return repository.readCat(id)
    }

    fun deleteCat(cat: Cat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCat(cat)
        }
    }

    override fun onCleared() {
        Log.d(TAG, "View model onClear")
        repository.close()
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onCleared()
    }

    private companion object {
        const val TAG = "CatsViewModel"
    }
}

class CatsViewModelFactory @Inject constructor(
    private val repository: CatRepository,
    private val preferences: SharedPreferences
    ): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun main() {
    var a = 7
    try {
        println("Try")
        //a / 0
    }
    catch (ex: Exception) {
        println("Catch")
    }
    finally {
        println("Finally")
    }

}